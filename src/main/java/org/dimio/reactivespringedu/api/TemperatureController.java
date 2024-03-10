package org.dimio.reactivespringedu.api;

import org.dimio.reactivespringedu.dto.Temperature;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
public class TemperatureController {
    private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

    @GetMapping("temperature-sse")
    public SseEmitter events() {
        var emitter = new SseEmitter();
        clients.add(emitter);
        emitter.onTimeout(() -> clients.remove(emitter));
        emitter.onCompletion(() -> clients.remove(emitter));
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
