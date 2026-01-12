package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.Comodidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComodidadeRepository extends JpaRepository <Comodidade, Long > {
    Comodidade findByNome(String nome);
}
