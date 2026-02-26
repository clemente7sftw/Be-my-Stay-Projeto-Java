package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.Avaliacao;
import com.bemystay.be_my_stay.repository.AvaliacaoRepository;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoService {
    private final AvaliacaoRepository avaliacaoRepository;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public void salvar(Avaliacao avaliacao) {

        avaliacaoRepository.save(avaliacao);
    }
}
