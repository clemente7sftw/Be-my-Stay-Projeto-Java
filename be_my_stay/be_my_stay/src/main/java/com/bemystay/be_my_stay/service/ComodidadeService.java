package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.Comodidade;
import com.bemystay.be_my_stay.repository.ComodidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComodidadeService {
    private final ComodidadeRepository repository;

    public ComodidadeService(ComodidadeRepository repository) {
        this.repository = repository;
    }
    public void salvar(Comodidade comodidade) {
        if (repository.findByNome(comodidade.getNome()) != null) {
            throw new RuntimeException("Já existe uma comodidade com esse nome");
        }
        repository.save(comodidade);
    }

    public List<Comodidade> listar() {
        return repository.findAtivas();
    }
    public void desativar(Long id) {
        Comodidade c = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comodidade não encontrada"));

        c.setAtivo(false);
        repository.save(c);
    }
    public Comodidade buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não encontrada"));
    }

}
