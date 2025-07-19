package com.shresthashreeson.RedisDemo.Integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;

import java.io.File;
import java.util.concurrent.Executors;

@Configuration
@IntegrationComponentScan
public class FileIntegrationConfig {

    private static final String DIRECTORY_TO_WATCH = System.getenv("WATCH_DIRECTORY");


    private final StringRedisTemplate redisTemplate;

    public FileIntegrationConfig(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Bean
    public FileReadingMessageSource fileReadingMessageSource(StringRedisTemplate redisTemplate) {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File(DIRECTORY_TO_WATCH));
        source.setFilter(new DistributedContentHashFilter(redisTemplate));
        return source;
    }

    @Bean
    public IntegrationFlow fileWatcherFlow(FileProcessor fileProcessor) {
        return IntegrationFlow
                .from(fileReadingMessageSource(redisTemplate),
                        c -> c.poller(Pollers.fixedDelay(1000)
                                .maxMessagesPerPoll(5)
                                .taskExecutor(Executors.newCachedThreadPool())))
                .handle(message -> {
                    File file = (File) message.getPayload();
                    fileProcessor.process(file);
                })
                .get();
    }
}
