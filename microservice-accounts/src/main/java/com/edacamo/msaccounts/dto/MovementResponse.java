package com.edacamo.msaccounts.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementResponse {

    private String numeroCuenta;
    private String tipo;
    private BigDecimal saldoInicial;
    private boolean estado;
    private String movimiento;

    @JsonIgnore
    private BigDecimal valor;

    @JsonIgnore
    private String tipoMovimiento;
}
