package org.dimio.reactivespringedu.service;

import io.reactivex.rxjava3.core.Observable;
import lombok.RequiredArgsConstructor;
import org.dimio.reactivespringedu.dto.Temperature;
import org.dimio.reactivespringedu.integration.RandomTemperatureProvider;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RxJavaTemperatureService {
    private final RandomTemperatureProvider sensor;

    private final Random rnd = new Random();
    private final Observable<Temperature> dataStream = Observable
            .range(0, Integer.MAX_VALUE)
            .concatMap(tick -> Observable
                    .just(tick)
                    .delay(rnd.nextInt(5), TimeUnit.SECONDS)
                    .map(__ -> this.probe())
            )
            .publish()
            .refCount();

    public Observable<Temperature> temperatureStream() {
        return dataStream;
    }

    private Temperature probe() {
        return new Temperature(sensor.measure());
    }

}
