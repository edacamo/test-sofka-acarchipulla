package com.edacamo.msaccounts.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movements {

    private Long id;
    private LocalDateTime fecha;
    private String tipoMovimiento;
    private String descripcion;
    private BigDecimal saldoInicial;
    private BigDecimal valor;
    private BigDecimal saldo;
    private Account account;

}
