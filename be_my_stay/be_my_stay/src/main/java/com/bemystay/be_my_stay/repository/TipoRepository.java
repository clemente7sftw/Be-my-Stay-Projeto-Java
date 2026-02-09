package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.TipoImovel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TipoRepository extends JpaRepository <TipoImovel, Long> {
    TipoImovel findByNome(String nome);
    @Query("SELECT t FROM TipoImovel t WHERE t.ativo = true")
    List<TipoImovel> findAtivos();
    @Query("SELECT t FROM TipoImovel t WHERE t.ativo = false")
    List<TipoImovel> findInativos();
    long countByAtivoTrue();
    long countByAtivoFalse();
    boolean existsByNomeIgnoreCase(String nome);
}
