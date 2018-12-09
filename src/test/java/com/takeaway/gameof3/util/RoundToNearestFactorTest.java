package com.takeaway.gameof3.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Shwetha on 09-12-2018.
 */
public class RoundToNearestFactorTest {

    @Test
    public void shouldRoundToNearestFactorof3() throws Exception {

        assertThat(RoundToNearestFactor.roundToNearestFactorOf3(5))
                .isEqualTo(6);

        assertThat(RoundToNearestFactor.roundToNearestFactorOf3(6))
                .isEqualTo(6);

        assertThat(RoundToNearestFactor.roundToNearestFactorOf3(4))
                .isEqualTo(3);

        assertThat(RoundToNearestFactor.roundToNearestFactorOf3(1))
                .isEqualTo(3);

        assertThat(RoundToNearestFactor.roundToNearestFactorOf3(0))
                .isEqualTo(3);

        assertThat(RoundToNearestFactor.roundToNearestFactorOf3(2))
                .isEqualTo(3);
    }
}