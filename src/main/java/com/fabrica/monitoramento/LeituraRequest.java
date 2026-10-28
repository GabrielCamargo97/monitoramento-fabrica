package com.fabrica.monitoramento;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record LeituraRequest(
        @NotBlank String maquina,
        double temperatura,
        @Min(0) int pecas
) {
}