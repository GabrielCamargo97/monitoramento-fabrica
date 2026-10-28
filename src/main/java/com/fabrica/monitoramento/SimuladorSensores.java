package com.fabrica.monitoramento;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SimuladorSensores {

    private static final int TOTAL_MAQUINAS = 4;

    private final LeituraRepository repository;
    private final LeituraStream stream;
    private final Random random = new Random();

    private final double[] temperaturas = new double[TOTAL_MAQUINAS];
    private final int[] pecas = new int[TOTAL_MAQUINAS];

    public SimuladorSensores(LeituraRepository repository, LeituraStream stream) {
        this.repository = repository;
        this.stream = stream;
        for (int i = 0; i < TOTAL_MAQUINAS; i++) {
            temperaturas[i] = 45 + random.nextDouble() * 10;
        }
    }

    @Scheduled(fixedRate = 2000)
    public void gerarLeituras() {
        for (int i = 0; i < TOTAL_MAQUINAS; i++) {
            // variação suave: sobe ou desce até 2 graus por ciclo
            temperaturas[i] += (random.nextDouble() - 0.5) * 4;

            // de vez em quando, uma máquina "esquenta" mais (útil para testar alertas)
            if (random.nextInt(60) == 0) {
                temperaturas[i] += 15;
            }

            // mantém a temperatura em uma faixa realista
            temperaturas[i] = Math.max(35, Math.min(95, temperaturas[i]));

            // peças: 0 a 3 novas por ciclo
            pecas[i] += random.nextInt(4);

            double temp = Math.round(temperaturas[i] * 10) / 10.0;
            Leitura salva = repository.save(new Leitura("Maquina " + (i + 1), temp, pecas[i]));
            stream.publicar(salva);
        }
    }
}