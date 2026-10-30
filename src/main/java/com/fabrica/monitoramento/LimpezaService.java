package com.fabrica.monitoramento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class LimpezaService {

    private static final Logger log = LoggerFactory.getLogger(LimpezaService.class);

    private final LeituraRepository repository;

    @Value("${limpeza.retencao-horas:24}")
    private long retencaoHoras;

    public LimpezaService(LeituraRepository repository) {
        this.repository = repository;
    }

    // roda a cada hora
    @Scheduled(fixedRate = 3600000, initialDelay = 60000)
    public void apagarLeiturasAntigas() {
        Instant limite = Instant.now().minus(Duration.ofHours(retencaoHoras));
        int apagadas = repository.apagarAnteriores(limite);
        if (apagadas > 0) {
            log.info("Limpeza: {} leituras com mais de {}h removidas", apagadas, retencaoHoras);
        }
    }
}