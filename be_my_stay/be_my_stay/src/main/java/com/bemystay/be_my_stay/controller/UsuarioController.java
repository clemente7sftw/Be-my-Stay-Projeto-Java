package com.bemystay.be_my_stay.controller;


import com.bemystay.be_my_stay.model.Usuario;
import com.bemystay.be_my_stay.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
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
    public String salvar(@ModelAttribute Usuario usuario, HttpSession session) {
        Usuario usuarioLogado = service.salvar(usuario);
        session.setAttribute("idUsuario", usuarioLogado.getId());

        return "redirect:/usuarios/telaInicial";
    }

    @GetMapping("/telaInicial")
    public String telaInicial(HttpSession session, Model model) {

        Long idUsuario = (Long) session.getAttribute("idUsuario");

        if (idUsuario == null) {
            return "redirect:/usuarios/cadastro";
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

        session.setAttribute("idUsuario", usuario.getId());

        return "redirect:/usuarios/telaInicial";
    }


}
