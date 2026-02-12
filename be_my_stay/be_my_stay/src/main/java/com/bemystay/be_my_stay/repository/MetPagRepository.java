package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.MetodoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetPagRepository extends JpaRepository<MetodoPagamento, Long> {
    boolean existsByTituloIgnoreCase(String titulo);
    MetodoPagamento findByTitulo(String titulo);
}
