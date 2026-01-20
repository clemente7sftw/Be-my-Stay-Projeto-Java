package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.Moderador;
import com.bemystay.be_my_stay.service.ModeradorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/moderadores")
public class ModeradorController {
    private final ModeradorService moderadorService;

    public ModeradorController(ModeradorService moderadorService) {
        this.moderadorService = moderadorService;
    }
    @GetMapping("/criar")
    public String criar(HttpSession session, Model model ) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("moderador", new Moderador());
        return "moderadores/addModerador";

    }
    @PostMapping("/salvar")
    public String salvar (@ModelAttribute Moderador moderador){

        moderadorService.salvar(moderador);

        return "redirect:/moderadores/listar";

    }
    @GetMapping("/listar")
    public String listar(HttpSession session,  Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("moderadores", moderadorService.listar());
        model.addAttribute("contarTotal", moderadorService.contarTotal());
        model.addAttribute("inativos", moderadorService.contarInativos());
        model.addAttribute("ativos", moderadorService.contarAtivos());

        return "moderadores/moderadores";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model){
        Moderador m = moderadorService.buscarPorId(id);
        model.addAttribute("moderador", m);
        return "moderadores/editar";

    }

    @PostMapping("/salvarEdicao/{id}")
    public String salvarEdicao(@PathVariable Long id,@ModelAttribute Moderador moderador){
        moderadorService.editar(id, moderador);
        return "redirect:/moderadores/listar";
    }
    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        moderadorService.desativar(id);
        return "redirect:/moderadores/listar";
    }
    @GetMapping("/listarInativas")
    public String listarInativas(HttpSession session,  Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("moderador", moderadorService.listarInativas());
        return "moderadores/restaurar";
    }
    @PostMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id){
        moderadorService.ativar(id);
        return "redirect:/moderadores/listar";
    }
}
