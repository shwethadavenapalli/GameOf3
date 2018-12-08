package com.takeaway.gameof3.domain;

import com.takeaway.gameof3.config.GameConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Shwetha on 08-12-2018.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {GameConfig.class})
@ComponentScan(basePackages = {"com.takeaway.gameof3"})
public class GameInitiatorTest {

    @Autowired
    private GameInitiator gameInitiator;

    @Test
    public void shouldGenerateRandomNumberAnd_MakeAtmost_3AttemptsToSendNumber_WhenPlayer2IsOffline() {
        gameInitiator.send();
        assertThat(gameInitiator.getMaxRetriedCount()).isEqualTo(3);
    }
}