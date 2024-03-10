package org.dimio.reactivespringedu.api;

import lombok.RequiredArgsConstructor;
import org.dimio.reactivespringedu.api.response.RxSseEmitter;
import org.dimio.reactivespringedu.dto.Temperature;
import org.dimio.reactivespringedu.service.RxJavaTemperatureService;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@SuppressWarnings("MVCPathVariableInspection")
@RestController
@RequiredArgsConstructor
public class SseTemperatureController {
    private final RxJavaTemperatureService rxJavaTemperatureService;

    private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

    @GetMapping("#{ '${app.endpoint.temperature.sse.base-path}'.concat('/app-events') }")
    public SseEmitter events() {
        var emitter = new SseEmitter();
        clients.add(emitter);
        emitter.onTimeout(() -> clients.remove(emitter));
        emitter.onCompletion(() -> clients.remove(emitter));
        return emitter;
    }

    @GetMapping("#{ '${app.endpoint.temperature.sse.base-path}'.concat('/rx') }")
    public SseEmitter rxStream() {
        var emitter = new RxSseEmitter();
        rxJavaTemperatureService.temperatureStream().subscribe(emitter.getSubscriber());
        return emitter;
    }

    @Async
    @EventListener
    protected void handleAppEvent(Temperature temperature) {
        Set<SseEmitter> dead = new HashSet<>();
        clients.forEach(emitter -> {
            try {
                emitter.send(temperature, MediaType.APPLICATION_JSON);
            } catch (Exception ignore) {
                dead.add(emitter);
            }
        });
        clients.removeAll(dead);
    }

}
