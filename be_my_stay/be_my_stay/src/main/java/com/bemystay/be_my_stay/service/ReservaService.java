package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.Imovel;
import com.bemystay.be_my_stay.model.Reserva;
import com.bemystay.be_my_stay.model.Usuario;
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

    public List<Reserva> reservasDoUsuario(Long idImovel, Long idUsuario) {
        return reservaRepository.buscarReservasDoUsuario(idImovel, idUsuario);
    }

    public List<Reserva> buscarReservasPorUsuario(Long idUsuario) {
        return reservaRepository.buscarPorUsuario(idUsuario);
    }

}
