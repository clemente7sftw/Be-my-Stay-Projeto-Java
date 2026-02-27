package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.EntregaChaves;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EntregaChavesRepository extends JpaRepository <EntregaChaves, Long> {
    @Query("SELECT c FROM EntregaChaves c WHERE c.ativo = true")
    List<EntregaChaves> findByAtivos();

    @Query("SELECT c FROM EntregaChaves c WHERE c.ativo = false")
    List<EntregaChaves> findByInativos();

    long countByAtivoTrue();

    long countByAtivoFalse();

    EntregaChaves findByTitulo(String titulo);

    boolean existsByTituloIgnoreCase(String titulo);
}
