package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.Usuario;
import com.bemystay.be_my_stay.repository.UsuarioRepository;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }


    public Usuario salvar(Usuario usuario) {
        return repository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }
    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }


}
