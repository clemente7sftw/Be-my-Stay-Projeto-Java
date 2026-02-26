package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.Avaliacao;
import com.bemystay.be_my_stay.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvaliacaoRepository  extends JpaRepository <Avaliacao, Long> {
    boolean existsByUsuario(Usuario usuario);
}
