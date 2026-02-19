package com.bemystay.be_my_stay.service;


import com.bemystay.be_my_stay.model.Comodidade;
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

    public List<MetodoPagamento> listarInativos() { return metPagRepository.findInativos(); }
    public void desativar(Long id) {
        MetodoPagamento m = metPagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(" não encontrado"));

        m.setAtivo(false);
        metPagRepository.save(m);
    }

    public void ativar(Long id) {
        MetodoPagamento m = metPagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(" não encontrado"));

        m.setAtivo(true);
        metPagRepository.save(m);
    }
    public void editar(Long id, MetodoPagamento dados) {

        MetodoPagamento m  = metPagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(" não encontrado"));

        m.setTitulo(dados.getTitulo());
        m.setCaminho(dados.getCaminho());

        metPagRepository.save(m);
    }
    public MetodoPagamento buscarPorId(Long id) {
        return metPagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não encontrada"));
    }

}
