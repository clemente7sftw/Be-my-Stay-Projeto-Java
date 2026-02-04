package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.Comodidade;
import com.bemystay.be_my_stay.model.Imovel;
import com.bemystay.be_my_stay.repository.ComodidadeRepository;
import com.bemystay.be_my_stay.repository.ImovelRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@Service
public class ImovelService {
    private final ImovelRepository imovelRepository;
    private final ComodidadeRepository comodidadeRepository;

    public ImovelService(ImovelRepository imovelRepository, ComodidadeRepository comodidadeRepository) {
        this.imovelRepository = imovelRepository;
        this.comodidadeRepository = comodidadeRepository;
    }
    public List<Imovel> listar() { return imovelRepository.findByAtivos(); }
    public List<Imovel> listarInativas() {
        return imovelRepository.findByInativos();
    }
    public long contarTotal() {
        return imovelRepository.count();
    }

    public long contarAtivos() {
        return imovelRepository.countByAtivoTrue();
    }

    public long contarInativos() {
        return imovelRepository.countByAtivoFalse();
    }

    public Imovel buscarPorId(Long id) {
        return imovelRepository.findById(id).orElseThrow(() -> new RuntimeException("Não encontrada"));
    }
    public void desativar(Long id) {
        Imovel i = imovelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("mensagem"));

        i.setAtivo(false);
        imovelRepository.save(i);
    }
    public void ativar(Long id) {
        Imovel i = imovelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("mensagem"));

        i.setAtivo(true);
        imovelRepository.save(i);
    }

    @Transactional

    public Imovel salvar(Imovel imovel){
        if (imovel.getEndereco() != null) {
            imovel.getEndereco().setImovel(imovel);
        }
        if (imovel.getDisponibilidadeInicio() != null
                && imovel.getDisponibilidadeInicio() != null
                && imovel.getDisponibilidadeFim().isBefore(imovel.getDisponibilidadeInicio())) {

            throw new IllegalArgumentException(
                    "Data final não pode ser menor que a inicial"
            );
        }

        return imovelRepository.save(imovel);
    }
    public List<Imovel> buscarPorId(List<Long> id) {
        return imovelRepository.findAllById(id);
    }



        public String buscarCep(String cep) {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            return restTemplate.getForObject(url, String.class);

    }




}
