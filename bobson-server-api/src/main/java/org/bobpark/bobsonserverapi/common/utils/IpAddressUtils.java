package org.bobpark.bobsonserverapi.common.utils;

import java.util.Enumeration;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.apache.commons.lang3.StringUtils;

public interface IpAddressUtils {

    List<String> IP_ADDRESS_HEADER_NAMES =
        List.of(
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR");

    static String getIpAddress() {

        HttpServletRequest request =
            ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

        String ipAddress = "";

        for (String headerName : IP_ADDRESS_HEADER_NAMES) {
            Enumeration<String> headerValues = request.getHeaders(headerName);

            if (headerValues.hasMoreElements()) {
                ipAddress = headerValues.nextElement();
                break;
            }

        }

        if (StringUtils.isBlank(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }
}
