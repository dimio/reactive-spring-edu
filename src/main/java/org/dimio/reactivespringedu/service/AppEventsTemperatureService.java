package org.dimio.reactivespringedu.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.dimio.reactivespringedu.dto.Temperature;
import org.dimio.reactivespringedu.integration.RandomTemperatureProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AppEventsTemperatureService {
    private final ApplicationEventPublisher publisher;
    private final RandomTemperatureProvider sensor;

    private final Random rnd = new Random();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void startProbe() {
        executor.schedule(this::probe, 1, TimeUnit.SECONDS);
    }

    private void probe() {
        publisher.publishEvent(new Temperature(sensor.measure()));
        executor.schedule(this::probe, rnd.nextInt(5), TimeUnit.SECONDS);
    }

}
