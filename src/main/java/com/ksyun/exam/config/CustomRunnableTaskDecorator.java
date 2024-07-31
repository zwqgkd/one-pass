package com.ksyun.exam.config;

import com.ksyun.exam.uitl.LogUtil;
import org.springframework.core.task.TaskDecorator;

public class CustomRunnableTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        String requestId = LogUtil.getRequestId();
        return () -> {
            try {
                LogUtil.startTrace(requestId);
                runnable.run();
            } finally {
                LogUtil.stopTrace();
            }
        };
    }
}