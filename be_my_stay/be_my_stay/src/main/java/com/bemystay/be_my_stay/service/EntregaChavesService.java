package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.EntregaChaves;
import com.bemystay.be_my_stay.repository.EntregaChavesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntregaChavesService {
    private final EntregaChavesRepository entregaChavesRepository;

    public EntregaChavesService(EntregaChavesRepository entregaChavesRepository) {
        this.entregaChavesRepository = entregaChavesRepository;
    }
    public void salvar(EntregaChaves entregaChaves) {
        if (entregaChavesRepository.findByTitulo(entregaChaves.getTitulo()) != null) {
            throw new RuntimeException("");
        }
        entregaChavesRepository.save(entregaChaves);
    }

    public List<EntregaChaves> listar() { return entregaChavesRepository.findByAtivos(); }

    public List<EntregaChaves> listarInativas() {
        return entregaChavesRepository.findByInativos();
    }

    public long contarTotal() {
        return entregaChavesRepository.count();
    }

    public long contarAtivos() {
        return entregaChavesRepository.countByAtivoTrue();
    }

    public long contarInativos() {
        return entregaChavesRepository.countByAtivoFalse();
    }


    public void desativar(Long id) {
        EntregaChaves c = entregaChavesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(""));

        c.setAtivo(false);
        entregaChavesRepository.save(c);
    }
    public void ativar(Long id){
        EntregaChaves c  = entregaChavesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(""));

        c.setAtivo(true);
        entregaChavesRepository.save(c);
    }
    public EntregaChaves buscarPorId(Long id) {
        return entregaChavesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não encontrada"));
    }
    public void editar(Long id, EntregaChaves dados) {

        EntregaChaves c = entregaChavesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(" não encontrada"));

        c.setTitulo(dados.getTitulo());
        c.setDescricao(dados.getDescricao());
        c.setIcone(dados.getIcone());


        entregaChavesRepository.save(c);
    }


}
