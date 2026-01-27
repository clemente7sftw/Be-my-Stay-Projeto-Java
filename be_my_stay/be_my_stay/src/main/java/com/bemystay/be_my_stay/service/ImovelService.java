package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.Comodidade;
import com.bemystay.be_my_stay.model.Imovel;
import com.bemystay.be_my_stay.repository.ComodidadeRepository;
import com.bemystay.be_my_stay.repository.ImovelRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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

    public Imovel salvar(Imovel imovel, List<Long> idComodidade){
        if (imovel.getEndereco() != null) {
            imovel.getEndereco().setImovel(imovel);
        }

        if (idComodidade != null && !idComodidade.isEmpty()) {
            List<Comodidade> comodidades =
                    comodidadeRepository.findAllById(idComodidade);
            imovel.setComodidades(comodidades);
        } else {
            imovel.getComodidades().clear();
        }

        return imovelRepository.save(imovel);
    }
}
