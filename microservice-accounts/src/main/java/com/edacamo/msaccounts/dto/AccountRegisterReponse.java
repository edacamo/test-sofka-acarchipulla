package com.edacamo.msaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRegisterReponse {

    private String numerCuenta;
    private String tipo;
    private BigDecimal saldoInicial;
    private boolean estado;
    private String cliente;
}
