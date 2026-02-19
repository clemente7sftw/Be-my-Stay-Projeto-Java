package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.MetodoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MetPagRepository extends JpaRepository<MetodoPagamento, Long> {
    boolean existsByTituloIgnoreCase(String titulo);
    MetodoPagamento findByTitulo(String titulo);
    @Query("SELECT m FROM MetodoPagamento m WHERE m.ativo = true")
    List<MetodoPagamento> findAtivos();
    @Query("SELECT m FROM MetodoPagamento m WHERE m.ativo = false")
    List<MetodoPagamento> findInativos();
}
