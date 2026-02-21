package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.Usuario;
import com.bemystay.be_my_stay.service.ModeradorService;
import com.bemystay.be_my_stay.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UsuarioService usuarioService;
    private final ModeradorService moderadorService;

    public AdminController(UsuarioService usuarioService, ModeradorService moderadorService) {
        this.usuarioService = usuarioService;
        this.moderadorService = moderadorService;
    }

    @GetMapping("/telaInicialAdm")
    public String telaInicialAdm(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        model.addAttribute("usuarioLogado", usuario);

        model.addAttribute("contarUsuarios", usuarioService.contarAtivos());
        model.addAttribute("contarModeradores", moderadorService.contarAtivos());
        model.addAttribute("contarImoveis", usuarioService.contarTotal());
        model.addAttribute("imovel", usuarioService.listar());

        return "admin/telaAdmin";
    }

    @GetMapping("/cadastroAdmin")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/cadastroAdmin";
    }
    @PostMapping("/cadastrarAdmin")
    public String cadastrarAdmin(@ModelAttribute Usuario usuario, HttpSession session) {

        Usuario usuarioLogado = usuarioService.cadastrarAdmin(usuario);

        session.setAttribute("usuarioLogado", usuarioLogado);

        return "redirect:/admin/telaAdmin";
    }

    @GetMapping("/telaAdmin")
    public String telaAdmin(HttpSession session, Model model) {

        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        model.addAttribute("usuarioLogado", usuario);

        return "admin/telaAdmin";
    }
    @GetMapping("/perfilAdmin")
    public String perfilAdmin(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        model.addAttribute("usuario", usuario);


        return "admin/perfilAdmin";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario u = usuarioService.buscarPorId(id);
        model.addAttribute("usuario", u);

        return "admin/perfilAdminEdicao";

    }
    @PostMapping("/salvarEdicao/{id}")
    public String salvarEdicao(
            @PathVariable Long id,
            @ModelAttribute Usuario usuario,
            @RequestParam(value = "arquivo", required = false) MultipartFile file
    ) throws IOException {

        if (file != null && !file.isEmpty()) {
            String nome = file.getOriginalFilename();

            Path caminho = Paths.get(
                    "src/main/resources/static/uploads/fotos_perfil/" + nome
            );

            Files.createDirectories(caminho.getParent());
            Files.copy(file.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);

            usuario.setFoto_perfil("fotos_perfil/" + nome);
        }


        usuarioService.editar(id, usuario);

        return "redirect:/usuarios/perfilAdmin";
    }






}
