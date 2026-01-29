package com.bemystay.be_my_stay.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "imoveis")
public class Imovel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imovel")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinTable(
            name = "imovel_tipo",
            joinColumns = @JoinColumn(name = "id_imovel"),
            inverseJoinColumns = @JoinColumn(name = "id_tipo_imovel")
    )
    private TipoImovel tipoImovel;

    @ManyToOne
    @JoinTable(
            name = "imovel_tipolugar",
            joinColumns = @JoinColumn(name = "id_imovel"),
            inverseJoinColumns = @JoinColumn(name = "id_tipo_lugar")
    )

    private TipoLugar tipoLugar;


    @OneToMany(mappedBy = "imovel", cascade = CascadeType.ALL)
    private List<Imagem> imagens = new ArrayList<>();

    @Column(nullable = false)
    private String titulo;

    @Column(name = "descricao_completa", columnDefinition = "TEXT")
    private String descricaoCompleta;

    @Column(name = "preco_diaria", nullable = false)
    private BigDecimal precoDiaria;

    @Column(name = "qtd_hospede")
    private Integer qtdHospede;

    @Column(name = "qtd_quartos")
    private Integer qtdQuartos;

    @Column(name = "qtd_camas")
    private Integer qtdCamas;

    @Column(name = "qtd_banheiros")
    private Integer qtdBanheiros;


    @Column(name = "vagas_garagem")
    private Integer vagasGaragem;

    @Column(name = "checkin")
    private LocalTime checkIn;

    @Column(name = "checkout")
    private LocalTime checkOut;

    @Column(nullable = false)
    private boolean ativo = true;


    @Column(name = "disponibilidade_inicio")
    private LocalDate disponibilidadeInicio;

    @Column(name = "disponibilidade_fim")
    private LocalDate disponibilidadeFim;

    public LocalDate getDisponibilidadeInicio() {
        return disponibilidadeInicio;
    }

    public void setDisponibilidadeInicio(LocalDate disponibilidadeInicio) {
        this.disponibilidadeInicio = disponibilidadeInicio;
    }

    public LocalDate getDisponibilidadeFim() {
        return disponibilidadeFim;
    }

    public void setDisponibilidadeFim(LocalDate disponibilidadeFim) {
        this.disponibilidadeFim = disponibilidadeFim;
    }

    @Column(name = "data_cad")
    private LocalDateTime dataCadastro;

    public LocalTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalTime checkOut) {
        this.checkOut = checkOut;
    }

    @ManyToMany
    @JoinTable(
            name = "imovel_comodidades",
            joinColumns = @JoinColumn(name = "id_imovel"),
            inverseJoinColumns = @JoinColumn(name = "id_comodidade")
    )

    private List<Comodidade> comodidade = new ArrayList<>();

    @OneToOne(mappedBy = "imovel", cascade = CascadeType.ALL)
    private Endereco endereco;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public TipoImovel getTipoImovel() {
        return tipoImovel;
    }

    public void setTipoImovel(TipoImovel tipoImovel) {
        this.tipoImovel = tipoImovel;
    }

    public TipoLugar getTipoLugar() {
        return tipoLugar;
    }

    public void setTipoLugar(TipoLugar tipoLugar) {
        this.tipoLugar = tipoLugar;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public String getDescricaoCompleta() {
        return descricaoCompleta;
    }

    public void setDescricaoCompleta(String descricaoCompleta) {
        this.descricaoCompleta = descricaoCompleta;
    }

    public BigDecimal getPrecoDiaria() {
        return precoDiaria;
    }

    public void setPrecoDiaria(BigDecimal precoDiaria) {
        this.precoDiaria = precoDiaria;
    }


    public Integer getQtdQuartos() {
        return qtdQuartos;
    }

    public void setQtdQuartos(Integer qtdQuartos) {
        this.qtdQuartos = qtdQuartos;
    }

    public Integer getQtdCamas() {
        return qtdCamas;
    }

    public Integer getQtdHospede() {
        return qtdHospede;
    }

    public void setQtdHospede(Integer qtdHospede) {
        this.qtdHospede = qtdHospede;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void setQtdCamas(Integer qtdCamas) {
        this.qtdCamas = qtdCamas;
    }

    public Integer getQtdBanheiros() {
        return qtdBanheiros;
    }

    public void setQtdBanheiros(Integer qtdBanheiros) {
        this.qtdBanheiros = qtdBanheiros;
    }

    public Integer getVagasGaragem() {
        return vagasGaragem;
    }

    public void setVagasGaragem(Integer vagasGaragem) {
        this.vagasGaragem = vagasGaragem;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<Comodidade> getComodidade() {
        return comodidade;
    }

    public void setComodidade(List<Comodidade> comodidade) {
        this.comodidade = comodidade;
    }

    public Endereco getEndereco() {
        return endereco;
    }


    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

}
