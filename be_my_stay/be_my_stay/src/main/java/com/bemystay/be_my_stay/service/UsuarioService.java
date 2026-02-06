package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.Cargo;
import com.bemystay.be_my_stay.model.Comodidade;
import com.bemystay.be_my_stay.model.Usuario;
import com.bemystay.be_my_stay.repository.CargoRepository;
import com.bemystay.be_my_stay.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CargoRepository cargoRepository;

    private Usuario salvarComCargo(Usuario usuario, String nomeCargo) {

        Cargo cargo = cargoRepository.findByNome(nomeCargo).orElseThrow(() -> new RuntimeException("Cargo não encontrado"));

        usuario.getCargo().add(cargo);

        return usuarioRepository.save(usuario);
    }

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

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }
    public List<Usuario> listar(){ return usuarioRepository.findByAtivos(); }

    public List<Usuario> listarInativas(){ return usuarioRepository.findInativos();}

    public long contarTotal() {
        return usuarioRepository.count();
    }

    public long contarAtivos() {
        return usuarioRepository.countByAtivoTrue();
    }

    public long contarInativos() {
        return usuarioRepository.countByAtivoFalse();
    }


    public void editar(Long id, Usuario dados) {

        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("não encontrada"));

        u.setNome(dados.getNome());
        u.setUserName(dados.getUserName());
        u.setEmail(dados.getEmail());
        u.setTelefone(dados.getTelefone());

        if (dados.getFoto_perfil() != null) {
            u.setFoto_perfil(dados.getFoto_perfil());
        }
        usuarioRepository.save(u);


    }
    public void desativar(Long id) {
        Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Comodidade não encontrada"));

        u.setAtivo(false);
        usuarioRepository.save(u);
    }
    public void ativar(Long id){
        Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Comodidade não encontrada"));

        u.setAtivo(true);
        usuarioRepository.save(u);
    }


}
