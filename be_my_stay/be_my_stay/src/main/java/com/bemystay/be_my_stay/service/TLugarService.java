package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.TipoLugar;
import com.bemystay.be_my_stay.repository.TLugarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TLugarService {
    private final TLugarRepository tLugarRepository;

    public List<TipoLugar> listar() {return tLugarRepository.findAtivos();}

    public TLugarService(TLugarRepository tLugarRepository) {
        this.tLugarRepository = tLugarRepository;
    }

    public void salvar(TipoLugar tipoLugar) {
        if (tLugarRepository.findByTitulo(tipoLugar.getTitulo()) != null) {
            throw new RuntimeException("Já existe uma tipo de lugar com esse titulo");
        }
        tLugarRepository.save(tipoLugar);
    }
}
