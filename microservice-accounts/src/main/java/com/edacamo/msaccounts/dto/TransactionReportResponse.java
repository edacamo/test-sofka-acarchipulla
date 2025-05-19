package com.edacamo.msaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TransactionReportResponse {

    private String fecha;

    private String cliente;

    private String numeroCuenta;

    private String tipo;

    private BigDecimal saldoInicial;

    private Boolean estado;

    private BigDecimal movimiento;

    private BigDecimal saldoDisponible;
}
