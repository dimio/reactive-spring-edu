package org.dimio.reactivespringedu.api.response;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.observers.DefaultObserver;
import lombok.Getter;
import org.dimio.reactivespringedu.dto.Temperature;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Getter
public class RxSseEmitter extends SseEmitter {
    private static final long SSE_SESSION_TIMEOUT = 30 * 60 * 1000L;
    private final Observer<Temperature> subscriber;

    public RxSseEmitter() {
        super(SSE_SESSION_TIMEOUT);
        this.subscriber = new DefaultObserver<>() {
            @Override
            public void onNext(@NonNull Temperature temperature) {
                try {
                    RxSseEmitter.this.send(temperature);
                } catch (IOException ignore) {
                    cancel();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                cancel();
            }

            @Override
            public void onComplete() {
                cancel();
            }
        };
        onCompletion(subscriber::onComplete);
        onError(subscriber::onError);
    }

}
