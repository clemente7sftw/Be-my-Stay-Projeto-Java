package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.Moderador;
import com.bemystay.be_my_stay.repository.ModeradorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModeradorService {
    private final ModeradorRepository moderadorRepository;

    public ModeradorService(ModeradorRepository moderadorRepository) {
        this.moderadorRepository = moderadorRepository;
    }
    public void salvar(Moderador moderador) {
        if (moderadorRepository.findByNome(moderador.getNome()) != null) {
            throw new RuntimeException("");
        }
        moderador.setSenhaHash(moderador.getNome()+ "123");
        moderadorRepository.save(moderador);
    }

    public List<Moderador> listar() { return moderadorRepository.findByAtivos(); }

    public List<Moderador> listarInativas() {
        return moderadorRepository.findInativos();
    }

    public long contarTotal() {
        return moderadorRepository.count();
    }

    public long contarAtivos() {
        return moderadorRepository.countByAtivoTrue();
    }

    public long contarInativos() {
        return moderadorRepository.countByAtivoFalse();
    }


    public void desativar(Long id) {
        Moderador m = moderadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(""));

        m.setAtivo(false);
        moderadorRepository.save(m);
    }
    public void ativar(Long id){
        Moderador m  = moderadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(""));

        m.setAtivo(true);
        moderadorRepository.save(m);
    }
    public Moderador buscarPorId(Long id) {
        return moderadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não encontrada"));
    }
    public void editar(Long id, Moderador dados) {

        Moderador m  = moderadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(" não encontrada"));

        m.setNome(dados.getNome());
        m.setEmail(dados.getEmail());
        m.setTelefone(dados.getTelefone());

        moderadorRepository.save(m);
    }


}
