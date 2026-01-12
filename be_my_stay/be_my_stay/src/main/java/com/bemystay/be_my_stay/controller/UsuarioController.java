package com.bemystay.be_my_stay.controller;


import com.bemystay.be_my_stay.model.Cargo;
import com.bemystay.be_my_stay.model.Usuario;
import com.bemystay.be_my_stay.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/cadastroUsuario")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/cadastroUsuario";
    }


    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute Usuario usuario, HttpSession session) {

        Usuario usuarioLogado = service.cadastrarHospede(usuario);

        session.setAttribute("idUsuario", usuarioLogado.getId());
        return "redirect:/usuarios/telaInicial";
    }


    @GetMapping("/telaInicial")
    public String telaInicial(HttpSession session, Model model) {

        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        Usuario usuario = service.buscarPorId(idUsuario);
        model.addAttribute("usuarioLogado", usuario);

        return "usuarios/telaInicial";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "usuarios/login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String senhaHash,
            HttpSession session,
            Model model
    ) {

        Usuario usuario = service.buscarPorEmail(email);

        if (usuario == null) {
            model.addAttribute("erro", "Email não cadastrado");
            return "usuarios/login";
        }

        if (!usuario.getSenhaHash().equals(senhaHash)) {
            model.addAttribute("erro", "Senha incorreta");
            return "usuarios/login";
        }

        Set<Cargo> cargos = usuario.getCargo();

        String cargo = cargos.iterator().next().getNome();


        session.setAttribute("idUsuario", usuario.getId());
        session.setAttribute("cargo", cargo);

        switch (cargo) {
            case "admin":
                return "redirect:/usuarios/telaAdmin";

            case "hóspede":
            default:
                return "redirect:/usuarios/telaInicial";
        }
    }
}