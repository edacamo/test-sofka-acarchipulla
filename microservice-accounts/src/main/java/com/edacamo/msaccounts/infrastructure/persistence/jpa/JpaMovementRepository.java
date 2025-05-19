package com.edacamo.msaccounts.infrastructure.persistence.jpa;

import com.edacamo.msaccounts.infrastructure.persistence.entity.MovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface JpaMovementRepository  extends JpaRepository<MovementEntity, Long> {

    @Query("SELECT m FROM MovementEntity m WHERE m.account.client.clienteId = :clientId AND m.fecha BETWEEN :startDate AND :endDate")
    List<MovementEntity> findByAccountAndDateRange(
            @Param("clientId") String clientId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT m FROM MovementEntity m WHERE m.account.id = :cuentaId ORDER BY m.fecha ASC")
    List<MovementEntity> findByCuentaId(@Param("cuentaId") Long cuentaId);

}
