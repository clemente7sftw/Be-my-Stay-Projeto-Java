package com.bemystay.be_my_stay.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table (name = "imoveis")
public class Imovel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imovel")
    private Long id;

    private String titulo;
    private String descricaoCurta;
    private String descricaoCompleta;
    private BigDecimal precoDiaria;
    private Integer capacidadeMax;
    private Integer qntQuartos;
    private Integer qntCamas;
    private Integer qntBanheiros;
    private Integer vagasGaragem  ;
    private Integer maxNoites;
    private boolean ativo = true;
    private LocalDateTime dataCad;
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_tipo_imovel", nullable = false)
    private TipoImovel tipoImovel;

}
