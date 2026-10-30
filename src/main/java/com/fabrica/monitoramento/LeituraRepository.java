package com.fabrica.monitoramento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

public interface LeituraRepository extends JpaRepository<Leitura, Long> {

    List<Leitura> findTop20ByOrderByTimestampDesc();

    @Modifying
    @Transactional
    @Query("delete from Leitura l where l.timestamp < :limite")
    int apagarAnteriores(@Param("limite") Instant limite);
}