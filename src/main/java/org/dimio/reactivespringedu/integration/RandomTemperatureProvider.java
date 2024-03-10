package org.dimio.reactivespringedu.integration;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomTemperatureProvider {
    private final Random rnd = new Random();

    public double measure() {
        return 16 + rnd.nextGaussian() * 10;
    }

}
