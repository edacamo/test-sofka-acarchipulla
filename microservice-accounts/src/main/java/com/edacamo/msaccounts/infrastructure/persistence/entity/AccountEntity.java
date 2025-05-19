package com.edacamo.msaccounts.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="cuenta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", unique=true, nullable = false)
    private String numeroCuenta;

    @Column(name = "tipo_cuenta")
    private String tipo;

    @Column(name = "saldo_inicial")
    private BigDecimal saldoInicial;

    @Column()
    private Boolean estado;

    // Relación con el cliente (snapshot local)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", referencedColumnName = "clienteId", foreignKey = @ForeignKey(name = "fk_cuenta_cliente_snapshot"))
    private ClientSnapshotEntity client;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<MovementEntity> movements;

}
