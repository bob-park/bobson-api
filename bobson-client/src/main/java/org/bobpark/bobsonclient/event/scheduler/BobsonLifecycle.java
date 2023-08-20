package org.bobpark.bobsonclient.event.scheduler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ReflectionUtils;

import org.apache.commons.lang3.StringUtils;

import org.bobpark.bobsonclient.event.annotation.EventSourcingHandler;

@Slf4j
@RequiredArgsConstructor
public class BobsonLifecycle {

    private static final Map<String, Method> EVENT_TARGET_MAP = Collections.synchronizedMap(new HashMap<>());

    private final ApplicationContext applicationContext;

    @PostConstruct
    public void init() {

        List<String> beanNames =
            Arrays.stream(applicationContext.getBeanDefinitionNames())
                .filter(beanName -> !StringUtils.equalsIgnoreCase(beanName, "lifecycle"))
                .toList();

        for (String beanName : beanNames) {

            Object bean = applicationContext.getBean(beanName);

            Class<?> beanClass = bean.getClass();

            if (AopUtils.isAopProxy(bean)) {
                beanClass = AopUtils.getTargetClass(bean);
            }

            for (Method method : beanClass.getDeclaredMethods()) {

                if (!method.isAnnotationPresent(EventSourcingHandler.class)) {
                    continue;
                }

                Class<?>[] parameterTypes = method.getParameterTypes();

                if (parameterTypes.length == 0) {
                    continue;
                }

                Class<?> eventClass = parameterTypes[0];

                EVENT_TARGET_MAP.put(beanName, method);

                log.debug("add event sourcing handler. (event={})", eventClass.getSimpleName());

            }

        }
    }

    @Scheduled(fixedDelayString = "${bobson.client.fetch-time-ms}")
    public void executeEvent() throws Exception {
        for (Entry<String, Method> entry : EVENT_TARGET_MAP.entrySet()) {

            String beanName = entry.getKey();
            Method method = entry.getValue();

            Object bean = applicationContext.getBean(beanName);

            Class<?>[] parameterTypes = method.getParameterTypes();

            if (parameterTypes.length == 0) {
                continue;
            }

            Class<?> parameterType = parameterTypes[0];

            Constructor<?>[] declaredConstructors = parameterType.getDeclaredConstructors();

            if (declaredConstructors.length == 0) {
                continue;
            }

            ReflectionUtils.invokeMethod(method, bean, declaredConstructors[0].newInstance());

        }
    }

}
