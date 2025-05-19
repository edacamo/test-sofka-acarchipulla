package com.edacamo.mspersons.domain.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Client extends Person {

    private String clienteId;
    private String contrasenia;
    private Boolean estado;
}
