package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.Comodidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComodidadeRepository extends JpaRepository <Comodidade, Long > {
    Comodidade findByNome(String nome);
    @Query("SELECT c FROM Comodidade c WHERE c.ativo = true")
    List<Comodidade> findAtivas();
    @Query("SELECT c FROM Comodidade c WHERE c.ativo = false")
    List<Comodidade> findInativas();

}
