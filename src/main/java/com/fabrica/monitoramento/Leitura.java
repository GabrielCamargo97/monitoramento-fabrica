package com.fabrica.monitoramento;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class Leitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String maquina;
    private double temperatura;
    private int pecas;
    private Instant timestamp;

    protected Leitura() {
    }

    public Leitura(String maquina, double temperatura, int pecas) {
        this.maquina = maquina;
        this.temperatura = temperatura;
        this.pecas = pecas;
        this.timestamp = Instant.now();
    }

    public Long getId() { return id; }
    public String getMaquina() { return maquina; }
    public double getTemperatura() { return temperatura; }
    public int getPecas() { return pecas; }
    public Instant getTimestamp() { return timestamp; }
}