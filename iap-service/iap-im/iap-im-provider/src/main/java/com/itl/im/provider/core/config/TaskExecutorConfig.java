package com.itl.im.provider.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itl.im.provider.util.ThreadPoolExpansion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PreDestroy;

@Configuration
@Slf4j
public class TaskExecutorConfig {
    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public AsyncTaskExecutor pushTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("async-push-");
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        return executor;
    }


    @Bean
    public AsyncTaskExecutor feignTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("async-feign-");
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        return executor;
    }

    @Bean
    public ThreadPoolExpansion threadPoolExpansion() {
        ThreadPoolExpansion threadPoolExpansion = new ThreadPoolExpansion();
        threadPoolExpansion.start();
        log.info("--------------->创建自定义线程池");
        return threadPoolExpansion;
    }

    @PreDestroy
    public void destroyThreadPool() throws InterruptedException {
        log.info("--------------->销毁自定义线程池");
        threadPoolExpansion().stop();
    }
}