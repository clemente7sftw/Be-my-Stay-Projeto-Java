package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.TipoImovel;
import com.bemystay.be_my_stay.repository.TipoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoService {
    private final TipoRepository tipoRepository;
    public List<TipoImovel> listar() { return tipoRepository.findAtivos(); }
    public TipoService(TipoRepository tipoRepository) {
        this.tipoRepository = tipoRepository;
    }
    public void salvar(TipoImovel tipoImovel) {
        if (tipoRepository.findByNome(tipoImovel.getNome()) != null) {
            throw new RuntimeException("Já existe uma tipo de imóvel com esse nome");
        }
        tipoRepository.save(tipoImovel);
    }
}
