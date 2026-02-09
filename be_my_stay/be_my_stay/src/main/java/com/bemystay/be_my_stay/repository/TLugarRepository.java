package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.TipoLugar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TLugarRepository extends JpaRepository <TipoLugar, Long> {

    @Query("SELECT l FROM TipoLugar l WHERE l.ativo = true")
    List<TipoLugar> findAtivos();

    @Query("SELECT l FROM TipoLugar l WHERE l.ativo = false")
    List<TipoLugar> findInativos();

    TipoLugar findByTitulo(String titulo);

    long countByAtivoTrue();

    long countByAtivoFalse();

    boolean existsByNomeIgnoreCase(String titulo);



}
