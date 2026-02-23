package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.Reserva;
import com.bemystay.be_my_stay.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public boolean existeConflito(Long imovelId, LocalDate checkin, LocalDate checkout) {
        return !reservaRepository
                .buscarConflitos(imovelId, checkin, checkout)
                .isEmpty();
    }


    public void salvar(Reserva reserva) {

        reservaRepository.save(reserva);
    }

    public List<Reserva> buscarAtivasPorImovel(Long idImovel) {
        return reservaRepository.buscarReservasAtivas(idImovel);
    }

    public void desativar(Long id) {
        Reserva r = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(" não encontrada"));

        r.setAtivo(false);
        reservaRepository.save(r);
    }
    public void ativar(Long id) {
        Reserva r = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(" não encontrada"));

        r.setAtivo(true);
        reservaRepository.save(r);
    }
    public List<Reserva> listar() { return reservaRepository.findAtivos(); }



}
