package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.TipoImovel;
import com.bemystay.be_my_stay.model.TipoLugar;
import com.bemystay.be_my_stay.model.Usuario;
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
    public TipoLugar buscarPorId(Long id) {
        return tLugarRepository.findById(id).orElse(null);
    }
    public void editar(Long id, TipoLugar dados) {

        TipoLugar l = tLugarRepository.findById(id).orElseThrow(() -> new RuntimeException(""));

        l.setTitulo(dados.getTitulo());
        l.setDescricao(dados.getDescricao());

        tLugarRepository.save(l);
    }
    public void desativar(Long id){
        TipoLugar l = tLugarRepository.findById(id).orElseThrow(() -> new RuntimeException(""));

        l.setAtivo(false);
        tLugarRepository.save(l);
    }

    public void ativar(Long id){
        TipoLugar l= tLugarRepository.findById(id).orElseThrow(() -> new RuntimeException(""));

        l.setAtivo(true);
        tLugarRepository.save(l);
    }
    public List<TipoLugar> listarInativas() {
        return tLugarRepository.findInativos();
    }
    public long contarAtivos() {
        return tLugarRepository.countByAtivoTrue();
    }

    public long contarInativos() {

        return tLugarRepository.countByAtivoFalse();
    }
    public long contarTotal() {
        return tLugarRepository.count();
    }
}
