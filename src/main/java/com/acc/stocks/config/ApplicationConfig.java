package com.acc.stocks.config;

import com.acc.stocks.services.clients.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@SpringBootApplication
@ComponentScan(value = {"com.acc.stocks"}, excludeFilters = @ComponentScan.Filter(value = {Configuration.class}))
public class ApplicationConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(BaseService tradeService, BaseService inputService, BaseService guiService) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(3);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(false);
        threadPoolTaskExecutor.initialize();
        threadPoolTaskExecutor.execute(tradeService);
        threadPoolTaskExecutor.execute(inputService);
        threadPoolTaskExecutor.execute(guiService);
        return threadPoolTaskExecutor;
    }

}