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
    public List<Comodidade> buscarPorId(List<Long> id) {
        return comodidadeRepository.findAllById(id);
    }



        public String buscarCep(String cep) {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            return restTemplate.getForObject(url, String.class);

    }

}
