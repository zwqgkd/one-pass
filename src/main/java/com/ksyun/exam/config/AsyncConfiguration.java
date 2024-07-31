package com.ksyun.exam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AsyncConfiguration {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setAwaitTerminationSeconds(600);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setCorePoolSize(50);
        taskExecutor.setMaxPoolSize(60);
        taskExecutor.setQueueCapacity(200);
        taskExecutor.setKeepAliveSeconds(200);
        taskExecutor.setThreadNamePrefix("async-running-");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setTaskDecorator(new CustomRunnableTaskDecorator());
        taskExecutor.initialize();
        return taskExecutor;
    }
}