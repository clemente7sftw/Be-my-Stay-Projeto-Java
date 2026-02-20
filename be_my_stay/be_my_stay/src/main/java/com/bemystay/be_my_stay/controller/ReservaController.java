package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.service.ReservaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reserva")
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController( ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        reservaService.desativar(id);
        return "redirect:/imovel/minhasReservas";
    }
    @PostMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id){
        reservaService.ativar(id);
        return "redirect:/comodidades/listar";
    }
}
