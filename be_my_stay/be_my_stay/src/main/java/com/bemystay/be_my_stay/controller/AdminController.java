package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.Usuario;
import com.bemystay.be_my_stay.service.ModeradorService;
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
    private final UsuarioService usuarioService;
    private final ModeradorService moderadorService;

    public AdminController(UsuarioService usuarioService, ModeradorService moderadorService) {
        this.usuarioService = usuarioService;
        this.moderadorService = moderadorService;
    }


    @GetMapping("/cadastroAdmin")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/cadastroAdmin";
    }
    @PostMapping("/cadastrarAdmin")
    public String cadastrarAdmin(@ModelAttribute Usuario usuario, HttpSession session) {

        Usuario usuarioLogado = usuarioService.cadastrarAdmin(usuario);

        session.setAttribute("usuarioLogado", usuarioLogado);

        return "redirect:/usuarios/telaAdmin";
    }

    @GetMapping("/telaAdmin")
    public String telaAdmin(HttpSession session, Model model) {

        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        model.addAttribute("usuarioLogado", usuario);

        return "usuarios/telaAdmin";
    }

    @GetMapping("/perfilAdmin")
    public String perfil(HttpSession session, Model model) {

        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        return "usuarios/perfilAdmin";
    }






}
