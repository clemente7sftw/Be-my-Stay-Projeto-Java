package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.Imovel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImovelRepository extends JpaRepository <Imovel, Long> {
}
