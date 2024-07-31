package com.ksyun.exam.uitl;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class LogUtil {

    private static final String TRACE_ID_KEY = "requestId";

    public static String getRequestId() {
        return MDC.get(TRACE_ID_KEY);
    }

    public static void startTrace(String requestId) {
        if (StringUtils.isEmpty(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        MDC.put(TRACE_ID_KEY, requestId);
    }

    public static void startTrace() {
        if (StringUtils.isEmpty(getRequestId())) {
            MDC.put(TRACE_ID_KEY, UUID.randomUUID().toString());
        }
    }

    public static void stopTrace() {
        MDC.remove(TRACE_ID_KEY);
    }
}
