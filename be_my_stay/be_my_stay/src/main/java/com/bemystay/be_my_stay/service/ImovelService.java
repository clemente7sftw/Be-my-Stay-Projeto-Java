package com.bemystay.be_my_stay.service;

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

    public ImovelService(ImovelRepository imovelRepository) {
        this.imovelRepository = imovelRepository;
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
                    "A data final da disponibilidade do imóvel não pode ser menor que a inicial, tente novamente"
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



    public List<Imovel> listarAtivosPorTipo(Long tipoId) {
        return imovelRepository.findByTipoImovelIdAndAtivoTrue(tipoId);
    }

    public void editar(Long id, Imovel dados) {

        Imovel i = imovelRepository.findById(id).orElseThrow(() -> new RuntimeException(""));

        i.setTitulo(dados.getTitulo());
        i.setDescricaoCompleta(dados.getDescricaoCompleta());
        i.setPrecoDiaria(dados.getPrecoDiaria());
        i.setDisponibilidadeFim(dados.getDisponibilidadeFim());
        i.setDisponibilidadeInicio(dados.getDisponibilidadeInicio());
        i.setQtdHospede(dados.getQtdHospede());
        i.setQtdBanheiros(dados.getQtdBanheiros());
        i.setQtdCamas(dados.getQtdCamas());
        i.setComodidade(dados.getComodidade());
        imovelRepository.save(i);
    }


}
