package com.bemystay.be_my_stay.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    private String nome;
    private String email;
    private String userName;
    private String senhaHash;
    private String telefone;
    private String cpf;
    private boolean ativo = true;
    private LocalDateTime dataCad;
    private LocalDate dataNasc;

    public String getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(String foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    public void setCargo(Set<Cargo> cargo) {
        this.cargo = cargo;
    }
    @Column(name = "foto_perfil")
    private String foto_perfil;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuarios_cargos",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_cargo")
    )

    private Set<Cargo> cargo = new HashSet<>();

    public Set<Cargo> getCargo() {
        return cargo;
    }

    public void setCargos(Set<Cargo> cargos) {
        this.cargo = cargo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCad() {
        return dataCad;
    }

    public void setDataCad(LocalDateTime dataCad) {
        this.dataCad = dataCad;
    }

    public LocalDate getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(LocalDate dataNasc) {
        this.dataNasc = dataNasc;
    }
    @PrePersist
    public void prePersist() {
        this.dataCad = LocalDateTime.now();
    }

}
