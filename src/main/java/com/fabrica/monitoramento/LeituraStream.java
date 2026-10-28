package com.fabrica.monitoramento;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class LeituraStream {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter registrar() {
        SseEmitter emitter = new SseEmitter(0L); // 0 = sem timeout
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));
        return emitter;
    }

    public void publicar(Leitura leitura) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("leitura").data(leitura));
            } catch (Exception e) {
                emitters.remove(emitter);
            }
        }
    }
}