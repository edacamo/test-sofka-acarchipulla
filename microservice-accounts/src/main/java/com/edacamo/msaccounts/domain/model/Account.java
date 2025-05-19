package com.edacamo.msaccounts.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private Long id;
    private String numeroCuenta;
    private String tipo;
    private BigDecimal saldoInicial;
    private Boolean estado;
    private ClientSnapshot client;
    private List<Movements> movements;

    public Account(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
}
