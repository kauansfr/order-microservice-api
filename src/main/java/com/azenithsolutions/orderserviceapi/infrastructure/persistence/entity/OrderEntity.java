package com.azenithsolutions.orderserviceapi.infrastructure.persistence.entity;

import com.azenithsolutions.orderserviceapi.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pedido")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long id;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Column(name = "nome_comprador", length = 50)
    private String nomeComprador;

    @Column(name = "email_comprador", length = 50)
    private String emailComprador;

    @Column(name = "CNPJ", columnDefinition = "CHAR(14)")
    private String cnpj;

    @Column(name = "valor")
    private String valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "tel_celular", columnDefinition = "CHAR(11)")
    private String telCelular;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}