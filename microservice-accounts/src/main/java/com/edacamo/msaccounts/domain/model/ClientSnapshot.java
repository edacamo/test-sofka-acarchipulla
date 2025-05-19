package com.edacamo.msaccounts.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientSnapshot {

    private Long id;
    private String clienteId;
    private String nombre;
    private String genero;
    private int edad;
    private String identificacion;
    private String direccion;
    private String telefono;
    private Boolean estado;

    public ClientSnapshot(String clienteId) {
        this.clienteId = clienteId;
    }
}
