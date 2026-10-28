package com.fabrica.monitoramento;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SimuladorSensores {

    private static final int TOTAL_MAQUINAS = 4;

    private final LeituraRepository repository;
    private final LeituraStream stream;
    private final AlertaService alertaService;
    private final Random random = new Random();

    private final double[] temperaturas = new double[TOTAL_MAQUINAS];
    private final int[] pecas = new int[TOTAL_MAQUINAS];

    public SimuladorSensores(LeituraRepository repository, LeituraStream stream,
                             AlertaService alertaService) {
        this.repository = repository;
        this.stream = stream;
        this.alertaService = alertaService;
        for (int i = 0; i < TOTAL_MAQUINAS; i++) {
            temperaturas[i] = 45 + random.nextDouble() * 10;
        }
    }

    @Scheduled(fixedRate = 2000)
    public void gerarLeituras() {
        for (int i = 0; i < TOTAL_MAQUINAS; i++) {
            temperaturas[i] += (random.nextDouble() - 0.5) * 4;

            if (random.nextInt(60) == 0) {
                temperaturas[i] += 15;
            }

            temperaturas[i] = Math.max(35, Math.min(95, temperaturas[i]));
            pecas[i] += random.nextInt(4);

            double temp = Math.round(temperaturas[i] * 10) / 10.0;
            Leitura salva = repository.save(new Leitura("Maquina " + (i + 1), temp, pecas[i]));
            stream.publicar(salva);
            alertaService.verificar(salva);
        }
    }
}