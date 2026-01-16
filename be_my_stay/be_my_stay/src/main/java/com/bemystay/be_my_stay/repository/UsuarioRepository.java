package com.bemystay.be_my_stay.repository;


import com.bemystay.be_my_stay.model.Comodidade;
import com.bemystay.be_my_stay.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.ativo = true")
    List<Usuario> findByAtivos();

    @Query("SELECT u FROM Usuario u WHERE u.ativo = false")
    List<Usuario> findInativos();

    long countByAtivoTrue();

    long countByAtivoFalse();

}