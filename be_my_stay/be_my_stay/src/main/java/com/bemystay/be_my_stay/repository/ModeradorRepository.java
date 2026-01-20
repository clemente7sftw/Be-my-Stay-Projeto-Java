package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.Moderador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModeradorRepository extends JpaRepository <Moderador, Long> {
    @Query("SELECT m FROM Moderador m WHERE m.ativo = true")
    List<Moderador> findByAtivos();

    @Query("SELECT m FROM Moderador m WHERE m.ativo = false")
    List<Moderador> findInativos();

    long countByAtivoTrue();

    long countByAtivoFalse();

    Moderador findByNome(String nome);
}
