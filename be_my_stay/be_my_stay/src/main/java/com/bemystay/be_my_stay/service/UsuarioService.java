package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.Cargo;
import com.bemystay.be_my_stay.model.Usuario;
import com.bemystay.be_my_stay.repository.CargoRepository;
import com.bemystay.be_my_stay.repository.UsuarioRepository;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CargoRepository cargoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, CargoRepository cargoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cargoRepository = cargoRepository;
    }

    public Usuario cadastrarHospede(Usuario usuario) {
        return salvarComCargo(usuario, "hóspede");
    }


    public Usuario cadastrarAdmin(Usuario usuario) {
        return salvarComCargo(usuario, "admin");
    }
    private Usuario salvarComCargo(Usuario usuario, String nomeCargo) {

        Cargo cargo = cargoRepository.findByNome(nomeCargo).orElseThrow(() -> new RuntimeException("Cargo não encontrado"));

        usuario.getCargo().add(cargo);

        return usuarioRepository.save(usuario);
    }



    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }


}
