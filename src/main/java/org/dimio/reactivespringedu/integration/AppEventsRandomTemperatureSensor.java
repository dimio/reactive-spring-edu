package org.dimio.reactivespringedu.integration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.dimio.reactivespringedu.dto.Temperature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AppEventsRandomTemperatureSensor {
    private final ApplicationEventPublisher publisher;

    private final Random rnd = new Random();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void startProbe() {
        executor.schedule(this::probe, 1, TimeUnit.SECONDS);
    }

    private void probe() {
        var temperature = 16 + rnd.nextGaussian() * 10;
        publisher.publishEvent(new Temperature(temperature));

        executor.schedule(this::probe, rnd.nextInt(5), TimeUnit.SECONDS);
    }

}
