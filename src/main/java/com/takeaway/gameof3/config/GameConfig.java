package com.takeaway.gameof3.config;

import com.takeaway.gameof3.domain.GameInitiator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Shwetha on 08-12-2018.
 */
@Configuration
@ComponentScan(basePackages = {"com.takeaway.gameof3"})
public class GameConfig {

    private static final Logger log = LoggerFactory.getLogger(GameConfig.class);

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @ConditionalOnProperty("game.initiator")
    @Bean
    public CommandLineRunner schedulingRunner(TaskExecutor executor, GameInitiator gameInitiator) {
        return args -> executor.execute(gameInitiator);
    }
}
