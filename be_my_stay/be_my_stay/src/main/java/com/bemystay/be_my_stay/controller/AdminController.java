package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.Usuario;
import com.bemystay.be_my_stay.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuarios")
public class AdminController {
    private final UsuarioService service;

    public AdminController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/cadastroAdmin")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/cadastroAdmin";
    }
    @PostMapping("/cadastrarAdmin")
    public String cadastrarAdmin(@ModelAttribute Usuario usuario, HttpSession session) {

        Usuario usuarioLogado = service.cadastrarAdmin(usuario);

        session.setAttribute("usuarioLogado", usuarioLogado);

        return "redirect:/usuarios/telaAdmin";
    }

    @GetMapping("/telaAdmin")
    public String telaAdmin(HttpSession session, Model model) {

        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        Usuario usuario = service.buscarPorId(idUsuario);
        model.addAttribute("usuarioLogado", usuario);

        return "usuarios/telaAdmin";
    }




}
