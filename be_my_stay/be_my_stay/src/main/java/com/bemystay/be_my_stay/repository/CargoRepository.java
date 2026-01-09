package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CargoRepository extends JpaRepository<Cargo, Long> {

    Optional<Cargo> findByNome(String nome);
}