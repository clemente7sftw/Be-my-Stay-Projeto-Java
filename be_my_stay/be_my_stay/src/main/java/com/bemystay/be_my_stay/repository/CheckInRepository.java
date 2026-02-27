package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CheckInRepository extends JpaRepository <CheckIn, Long> {
    @Query("SELECT c FROM CheckIn c WHERE c.ativo = true")
    List<CheckIn> findByAtivos();

    @Query("SELECT c FROM CheckIn c WHERE c.ativo = false")
    List<CheckIn> findInativos();

    long countByAtivoTrue();

    long countByAtivoFalse();

    CheckIn findByTitulo(String titulo);

    boolean existsByTituloIgnoreCase(String titulo);
}
