package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.model.Imovel;
import com.bemystay.be_my_stay.model.Reserva;
import com.bemystay.be_my_stay.model.Usuario;
import com.bemystay.be_my_stay.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public void reservar(Imovel imovel, LocalDate checkin, LocalDate checkout,
                         int qtdHospedes, Usuario usuario) {


        System.out.println(">>> ENTROU NO MÉTODO reservar()");

        Reserva reserva = new Reserva();
        reserva.setImovel(imovel);
        reserva.setUsuario(usuario);
        reserva.setCheckin(checkin);
        reserva.setCheckout(checkout);
        reserva.setQtdHospedes(qtdHospedes);

        System.out.println(">>> VAI SALVAR RESERVA");
        reservaRepository.save(reserva);
        System.out.println(">>> SALVOU RESERVA");
    }
}
