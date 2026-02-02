package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.Imovel;
import com.bemystay.be_my_stay.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImovelRepository extends JpaRepository <Imovel, Long> {
    @Query("SELECT i FROM Imovel i WHERE i.ativo = true")
    List<Imovel> findByAtivos();

    @Query("SELECT i FROM Imovel  i WHERE i.ativo = true")
    List<Imovel> findByInativos();



}
