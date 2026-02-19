package com.bemystay.be_my_stay.service;


import com.bemystay.be_my_stay.model.MetodoPagamento;
import com.bemystay.be_my_stay.repository.MetPagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetPagService {
    private final MetPagRepository metPagRepository;

    public MetPagService(MetPagRepository metPagRepository) {
        this.metPagRepository = metPagRepository;
    }

    public void salvar(MetodoPagamento metodoPagamento) {
        if (metPagRepository.findByTitulo(metodoPagamento.getTitulo()) != null) {
            throw new RuntimeException("Já existe um método de pagamento com esse nome");
        }
        metPagRepository.save(metodoPagamento);
    }

    public List<MetodoPagamento> listar() { return metPagRepository.findAtivos(); }
}
