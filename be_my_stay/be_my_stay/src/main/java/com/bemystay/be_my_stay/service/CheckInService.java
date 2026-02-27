package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.CheckIn;
import com.bemystay.be_my_stay.repository.CheckInRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckInService {
    private final CheckInRepository checkInRepository;

    public CheckInService(CheckInRepository checkInRepository) {
        this.checkInRepository = checkInRepository;
    }
    public void salvar(CheckIn checkIn) {
        if (checkInRepository.findByTitulo(checkIn.getTitulo()) != null) {
            throw new RuntimeException("");
        }
        checkInRepository.save(checkIn);
    }

    public List<CheckIn> listar() { return checkInRepository.findByAtivos(); }

    public List<CheckIn> listarInativas() {
        return checkInRepository.findInativos();
    }

    public long contarTotal() {
        return checkInRepository.count();
    }

    public long contarAtivos() {
        return checkInRepository.countByAtivoTrue();
    }

    public long contarInativos() {
        return checkInRepository.countByAtivoFalse();
    }


    public void desativar(Long id) {
        CheckIn c = checkInRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(""));

        c.setAtivo(false);
        checkInRepository.save(c);
    }
    public void ativar(Long id){
        CheckIn c  = checkInRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(""));

        c.setAtivo(true);
        checkInRepository.save(c);
    }
    public CheckIn buscarPorId(Long id) {
        return checkInRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não encontrada"));
    }
    public void editar(Long id, CheckIn dados) {

        CheckIn c = checkInRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(" não encontrada"));

        c.setTitulo(dados.getTitulo());
        c.setDescricao(dados.getTitulo());
        c.setIcone(dados.getIcone());


        checkInRepository.save(c);
    }


}
