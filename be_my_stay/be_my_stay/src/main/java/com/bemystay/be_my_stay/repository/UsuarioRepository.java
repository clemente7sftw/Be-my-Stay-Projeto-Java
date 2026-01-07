package com.bemystay.be_my_stay.repository;


import com.bemystay.be_my_stay.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}