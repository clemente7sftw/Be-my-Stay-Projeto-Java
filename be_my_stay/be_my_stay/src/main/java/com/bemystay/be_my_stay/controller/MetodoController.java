package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.Comodidade;
import com.bemystay.be_my_stay.model.MetodoPagamento;
import com.bemystay.be_my_stay.repository.MetPagRepository;
import com.bemystay.be_my_stay.service.MetPagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/metodo")
public class MetodoController {
    private final MetPagService metPagService;
    private final MetPagRepository metPagRepository;

    public MetodoController(MetPagService metPagService, MetPagRepository metPagRepository) {
        this.metPagService = metPagService;
        this.metPagRepository = metPagRepository;
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        metPagService.desativar(id);
        return "redirect:/usuarios/listarMet";
    }
    @PostMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id){
        metPagService.ativar(id);
        return "redirect:/usuarios/listarMet";
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        MetodoPagamento m = metPagService.buscarPorId(id);
        model.addAttribute("método", m);
        return "metodoPag/editar";
    }

    @PostMapping("/salvarEdicao/{id}")
    public String salvarEdicao(@PathVariable Long id,@ModelAttribute MetodoPagamento metodoPagamento ) {

        metPagService.editar(id, metodoPagamento);
        return "redirect:/usuarios/listarMet";
    }
}
