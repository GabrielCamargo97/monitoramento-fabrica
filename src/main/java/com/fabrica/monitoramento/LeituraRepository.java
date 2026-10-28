package com.fabrica.monitoramento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeituraRepository extends JpaRepository<Leitura, Long> {

    List<Leitura> findTop20ByOrderByTimestampDesc();
}