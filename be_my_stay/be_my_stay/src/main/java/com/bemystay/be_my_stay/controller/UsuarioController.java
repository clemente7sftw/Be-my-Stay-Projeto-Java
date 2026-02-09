package com.bemystay.be_my_stay.controller;


import com.bemystay.be_my_stay.model.*;
import com.bemystay.be_my_stay.repository.ImovelRepository;
import com.bemystay.be_my_stay.repository.UsuarioRepository;
import com.bemystay.be_my_stay.service.ImovelService;
import com.bemystay.be_my_stay.service.ModeradorService;
import com.bemystay.be_my_stay.service.ReservaService;
import com.bemystay.be_my_stay.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService service;
    private final ModeradorService moderadorService;
    private final ImovelService imovelService;
    private final ImovelRepository imovelRepository;
    private final ReservaService reservaService;
    private final UsuarioRepository usuarioRepository;


    public UsuarioController(UsuarioService service, ModeradorService moderadorService, ImovelService imovelService, ImovelRepository imovelRepository, ReservaService reservaService, UsuarioRepository usuarioRepository) {
        this.service = service;
        this.moderadorService = moderadorService;
        this.imovelService = imovelService;
        this.imovelRepository = imovelRepository;
        this.reservaService = reservaService;
        this.usuarioRepository = usuarioRepository;
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

        return "usuarios/inicio";
    }
    @GetMapping("/anfitriao")
    public String anfitriao(HttpSession session, Model model){

        Long idUsuario = (Long) session.getAttribute("idUsuario");

        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        return "usuarios/anfitriao";
    }

    @GetMapping("/telaInicialAdm")
    public String telaInicialAdm(HttpSession session, Model model) {

        Long idUsuario = (Long) session.getAttribute("idUsuario");

        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        Usuario usuario = service.buscarPorId(idUsuario);

        model.addAttribute("usuarioLogado", usuario);

        model.addAttribute("contarUsuarios", service.contarAtivos());
        model.addAttribute("contarModeradores", moderadorService.contarAtivos());
        model.addAttribute("contarImoveis", imovelService.contarTotal());
        model.addAttribute("imovel", imovelService.listar());

        return "usuarios/telaAdmin";
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
            model.addAttribute("erro", "Email não cadastrado");
            return "usuarios/login";
        }

        if (!usuario.getSenhaHash().equals(senhaHash)) {
            model.addAttribute("erro", "Senha incorreta");
            return "usuarios/login";
        }

        String cargo = usuario.getCargo().stream()
                .map(Cargo::getNome)
                .findFirst()
                .orElse("hóspede");

        session.setAttribute("idUsuario", usuario.getId());
        session.setAttribute("cargo", cargo);

        if ("admin".equals(cargo)) {
            return "redirect:/usuarios/telaInicialAdm";

        }

        return "redirect:/usuarios/telaInicial";
    }

    @GetMapping("/listarUsuarios")public String listar(HttpSession session,  Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("usuarios", service.listar());
        model.addAttribute("contarTotal", service.contarTotal());
        model.addAttribute("ativas", service.contarAtivos());
        model.addAttribute("inativas", service.contarInativos());

        return "usuarios/usuarios";
    }


    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model){
        Usuario u = service.buscarPorId(id);
        model.addAttribute("usuario", u);

        return "usuarios/perfilAdminEdicao";

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

        service.editar(id, usuario);

        return "redirect:/usuarios/perfilAdmin";
    }

    @GetMapping("/listarInativas")
    public String listarInativas(HttpSession session,  Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("usuarios", service.listarInativas());
        return "usuarios/restaurarUsuarios";
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
    public String restaurar(@PathVariable Long id){
        service.ativar(id);
        return "redirect:/usuarios/listarUsuarios";
    }
    @GetMapping("/listarImoveis")
    public String listarImoveis(HttpSession session,  Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        model.addAttribute("imoveis", imovelService.listar());

        return "usuarios/inicio";
    }
    @GetMapping("/perfilAdmin")
    public String perfilAdmin(HttpSession session,  Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        Usuario usuario = service.buscarPorId(idUsuario);

        model.addAttribute("usuario", usuario);


        return "usuarios/perfilAdmin";
    }


    @PostMapping("/reservar/{id}")
    public String reservar(@PathVariable Long id,
                           @RequestParam LocalDate checkin,
                           @RequestParam LocalDate checkout,
                           HttpSession session,
                           Model model) {

        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        if (reservaService.existeConflito(id, checkin, checkout)) {
            model.addAttribute("erro", "Esse imóvel já está reservado nesse período.");
            model.addAttribute("imovel", imovelService.buscarPorId(id));
            return "imoveis/descricao";
        }

        Usuario usuario = service.buscarPorId(idUsuario);
        Imovel imovel = imovelService.buscarPorId(id);

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setImovel(imovel);
        reserva.setCheckin(checkin);
        reserva.setCheckout(checkout);

        reservaService.salvar(reserva);

        return "/usuarios/inicio";
    }


}