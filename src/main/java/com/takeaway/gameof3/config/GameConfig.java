package com.takeaway.gameof3.config;

import com.takeaway.gameof3.domain.GameInitiator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

/**
 * Created by Shwetha on 08-12-2018.
 */
@Configuration
public class GameConfig {

    private static final Logger log = LoggerFactory.getLogger(GameConfig.class);

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @ConditionalOnProperty("game.initiator")
    @Bean
    public CommandLineRunner schedulingRunner(TaskExecutor executor) {
        return new CommandLineRunner() {
            public void run(String... args) throws Exception {
                executor.execute(
                        new GameInitiator());
            }
        };
    }
}
