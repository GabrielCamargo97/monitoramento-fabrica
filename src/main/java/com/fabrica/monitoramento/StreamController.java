package com.fabrica.monitoramento;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class StreamController {

    private final LeituraStream stream;

    public StreamController(LeituraStream stream) {
        this.stream = stream;
    }

    @GetMapping("/api/stream")
    public SseEmitter stream() {
        return stream.registrar();
    }
}