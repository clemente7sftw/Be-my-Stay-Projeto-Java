package com.bemystay.be_my_stay.model;

import jakarta.persistence.*;

@Entity
@Table(name = "imagem_imoveis")
public class Imagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagem")
    private Long id;

    private String icone;

    private String caminho;
    @ManyToOne
    @JoinColumn(name = "id_imovel")
    private Imovel imovel;

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }
}
