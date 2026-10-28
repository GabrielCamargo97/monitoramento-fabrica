package com.fabrica.monitoramento;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leituras")
public class LeituraController {

    private final LeituraRepository repository;

    public LeituraController(LeituraRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Leitura receber(@Valid @RequestBody LeituraRequest req) {
        Leitura leitura = new Leitura(req.maquina(), req.temperatura(), req.pecas());
        return repository.save(leitura);
    }

    @GetMapping
    public List<Leitura> ultimas() {
        return repository.findTop20ByOrderByTimestampDesc();
    }
}