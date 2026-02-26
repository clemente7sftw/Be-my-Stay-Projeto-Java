package com.bemystay.be_my_stay.controller;


import com.bemystay.be_my_stay.model.*;
import com.bemystay.be_my_stay.repository.ImovelRepository;
import com.bemystay.be_my_stay.repository.MetPagRepository;
import com.bemystay.be_my_stay.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService service;
    private final ModeradorService moderadorService;
    private final ImovelService imovelService;
    private final ImovelRepository imovelRepository;
    private final ReservaService reservaService;
    private final MetPagRepository metPagRepository;
    private final TipoService tipoService;
    private final MetPagService metPagService;




    public UsuarioController(UsuarioService service, ModeradorService moderadorService, ImovelService imovelService, ImovelRepository imovelRepository, ReservaService reservaService, MetPagRepository metPagRepository, TipoService tipoService, MetPagService metPagService) {
        this.service = service;
        this.moderadorService = moderadorService;
        this.imovelService = imovelService;
        this.imovelRepository = imovelRepository;
        this.reservaService = reservaService;
        this.metPagRepository = metPagRepository;
        this.tipoService = tipoService;
        this.metPagService = metPagService;
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
        boolean temImovel = imovelRepository.existsByUsuario(usuario);
        model.addAttribute("temImovel", temImovel);
        model.addAttribute("usuarioLogado", usuario);
        model.addAttribute("imoveis", imovelService.listar());
        model.addAttribute("tipo_imovel", tipoService.listar());

        return "usuarios/inicio";
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
            Model model) {

        Usuario usuario = service.buscarPorEmail(email);

        if (usuario == null) {
            model.addAttribute("erro", "Dados incorretos");
            return "usuarios/login";
        }

        if (!usuario.getSenhaHash().equals(senhaHash)) {
            model.addAttribute("erro", "Dados incorretos");
            return "usuarios/login";
        }

        String cargo = usuario.getCargo().stream()
                .map(Cargo::getNome)
                .findFirst()
                .orElse("hóspede");

        session.setAttribute("idUsuario", usuario.getId());
        session.setAttribute("cargo", cargo);

        if ("admin".equals(cargo)) {
            return "redirect:/admin/telaInicialAdm";

        }

        return "redirect:/usuarios/telaInicial";
    }

    @GetMapping("/listarUsuarios")
    public String listar(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("usuarios", service.listar());
        model.addAttribute("contarTotal", service.contarTotal());
        model.addAttribute("ativas", service.contarAtivos());
        model.addAttribute("inativas", service.contarInativos());

        return "admin/usuarios";
    }


    @GetMapping("/listarInativas")
    public String listarInativas(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("usuarios", service.listarInativas());
        return "admin/restaurarUsuarios";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/usuarios/login";
    }


    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        service.desativar(id);
        return "redirect:/usuarios/listarUsuarios";
    }

    @PostMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id) {
        service.ativar(id);
        return "redirect:/usuarios/listarUsuarios";
    }


}