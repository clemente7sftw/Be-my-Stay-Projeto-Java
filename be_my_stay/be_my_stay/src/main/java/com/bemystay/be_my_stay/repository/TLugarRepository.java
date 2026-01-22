package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.TipoLugar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TLugarRepository extends JpaRepository <TipoLugar, Long> {
}
