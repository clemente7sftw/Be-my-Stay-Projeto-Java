package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.Usuario;
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
@RequestMapping("/hospedes")
public class HospedeController {
    private final UsuarioService usuarioService;

    public HospedeController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        model.addAttribute("usuario", usuario);


        return "usuarios/perfil";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario u = usuarioService.buscarPorId(id);
        model.addAttribute("usuario", u);

        return "usuarios/perfilEdicao";

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

        return "redirect:/hospedes/perfil";
    }



}
