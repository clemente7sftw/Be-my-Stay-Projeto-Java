package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.TipoImovel;
import com.bemystay.be_my_stay.repository.TipoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoService {
    private final TipoRepository tipoRepository;

    public TipoService(TipoRepository tipoRepository) {
        this.tipoRepository = tipoRepository;
    }

    public List<TipoImovel> listar() { return tipoRepository.findAtivos(); }


    public long contarTotal() {
        return tipoRepository.count();
    }

    public long contarAtivos() {
        return tipoRepository.countByAtivoTrue();
    }

    public long contarInativos() {
        return tipoRepository.countByAtivoFalse();
    }

    public List<TipoImovel> listarInativas() {
        return tipoRepository.findInativos();
    }

    public void salvar(TipoImovel tipoImovel) {
        if (tipoRepository.findByNome(tipoImovel.getNome()) != null) {
            throw new RuntimeException("Já existe uma tipo de imóvel com esse nome");
        }
        tipoRepository.save(tipoImovel);
    }
    public TipoImovel buscarPorId(Long id) {
        return tipoRepository.findById(id).orElse(null);
    }

    public void editar (Long id, TipoImovel dados ){
        TipoImovel t = tipoRepository.findById(id).orElseThrow(() -> new RuntimeException(""));

        t.setNome(dados.getNome());
        t.setIcone(dados.getIcone());

        tipoRepository.save(t);
    }

    public void desativar(Long id){
        TipoImovel t = tipoRepository.findById(id).orElseThrow(() -> new RuntimeException(""));

        t.setAtivo(false);
        tipoRepository.save(t);
    }

    public void ativar(Long id){
        TipoImovel t = tipoRepository.findById(id).orElseThrow(() -> new RuntimeException(""));

        t.setAtivo(true);
        tipoRepository.save(t);
    }

}
