package org.bobpark.bobsonclient.configure.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class BobsonClientCondition implements Condition {

    private static final String PROPERTY_ENABLE = "bobson.client.enabled";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getEnvironment().getProperty(PROPERTY_ENABLE, Boolean.class, true);
    }
}
