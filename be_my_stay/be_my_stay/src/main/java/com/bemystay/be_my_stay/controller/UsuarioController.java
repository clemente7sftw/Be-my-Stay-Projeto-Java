package com.bemystay.be_my_stay.controller;


import com.bemystay.be_my_stay.model.Usuario;
import com.bemystay.be_my_stay.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/cadastro")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/cadastro";
    }

    @PostMapping("/cadastrar")
    public String salvar(@ModelAttribute Usuario usuario) {
        service.salvar(usuario);
        return "redirect:/usuarios/cadastro";
    }
}
