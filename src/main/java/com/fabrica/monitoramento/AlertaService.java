package com.fabrica.monitoramento;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AlertaService {

    private final Notificador notificador;
    private final Map<String, Instant> ultimoAlerta = new ConcurrentHashMap<>();

    @Value("${alerta.limite:80}")
    private double limite;

    @Value("${alerta.cooldown-minutos:2}")
    private long cooldownMinutos;

    public AlertaService(Notificador notificador) {
        this.notificador = notificador;
    }

    public void verificar(Leitura leitura) {
        if (leitura.getTemperatura() <= limite) {
            return;
        }

        Instant agora = Instant.now();
        Instant ultimo = ultimoAlerta.get(leitura.getMaquina());

        if (ultimo != null && Duration.between(ultimo, agora).toMinutes() < cooldownMinutos) {
            return; // ainda em cooldown para esta máquina
        }

        ultimoAlerta.put(leitura.getMaquina(), agora);
        notificador.enviar(String.format(
                "⚠ Atenção: %s superaqueceu! Temperatura: %.1f°C",
                leitura.getMaquina(), leitura.getTemperatura()));
    }
}