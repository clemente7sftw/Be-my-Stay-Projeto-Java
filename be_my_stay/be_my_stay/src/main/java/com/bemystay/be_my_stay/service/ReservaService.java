package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.config.reservaException;
import com.bemystay.be_my_stay.model.Reserva;
import com.bemystay.be_my_stay.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {


    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }


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

    public List<Reserva> listar() {
        return reservaRepository.findAtivos();
    }

    public void verificarDisponibilidade(Long id,
                                         LocalDate checkin,
                                         LocalDate checkout) {

        if (!checkout.isAfter(checkin)) {
            throw new reservaException("Checkout deve ser após Checkin");
        }

        boolean existe = reservaRepository
                .existeReservaConflitante(id, checkin, checkout);

        if (existe) {
            throw new reservaException("Data Indisponível");
        }
    }

    public void verificarExcluir(Long imovelId) {

        LocalDate hoje = LocalDate.now();

        boolean existe = reservaRepository
                .existeReservaFutura(imovelId, hoje);

        if (existe) {
            throw new reservaException(
                    "O imóvel não pode ser excluído pois possui reservas futuras."
            );
        }
    }

}
