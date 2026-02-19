package com.bemystay.be_my_stay.controller;


import com.bemystay.be_my_stay.model.*;
import com.bemystay.be_my_stay.repository.ImovelRepository;
import com.bemystay.be_my_stay.repository.MetPagRepository;
import com.bemystay.be_my_stay.repository.UsuarioRepository;
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

    @GetMapping("/anfitriao")
    public String anfitriao(HttpSession session, Model model) {

        Long idUsuario = (Long) session.getAttribute("idUsuario");

        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        Usuario usuario = service.buscarPorId(idUsuario);
        List<Imovel> imoveis = imovelRepository.findByUsuarioIdAndAtivoTrue(idUsuario);
        model.addAttribute("imoveis", imoveis);
        model.addAttribute("usuarioLogado", usuario);

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

        return "usuarios/usuarios";
    }


    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
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
    public String listarInativas(HttpSession session, Model model) {
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
    public String restaurar(@PathVariable Long id) {
        service.ativar(id);
        return "redirect:/usuarios/listarUsuarios";
    }

    @GetMapping("/listarImoveis")
    public String listarImoveis(HttpSession session, Model model, @RequestParam(required = false) Long tipo) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        List<Imovel> imoveis;

        if (tipo != null) {
            imoveis = imovelService.listarAtivosPorTipo(tipo);
        } else {
            imoveis = imovelService.listar();
        }

        model.addAttribute("imoveis", imoveis);
        model.addAttribute("tipo_imovel", tipoService.listar());

        return "usuarios/inicio";
    }

    @GetMapping("/perfilAdmin")
    public String perfilAdmin(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        Usuario usuario = service.buscarPorId(idUsuario);

        model.addAttribute("usuario", usuario);


        return "usuarios/perfilAdmin";
    }

    @PostMapping("/reservar/{id}/confirmar")
    public String confirmarReserva(@PathVariable Long id,
                                   @RequestParam LocalDate checkin,
                                   @RequestParam LocalDate checkout,
                                   @RequestParam Integer qtdHospede,
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

        Imovel imovel = imovelService.buscarPorId(id);

        long dias = ChronoUnit.DAYS.between(checkin, checkout);
        BigDecimal total = imovel.getPrecoDiaria()
                .multiply(BigDecimal.valueOf(dias));

        model.addAttribute("imovel", imovel);
        model.addAttribute("checkin", checkin);
        model.addAttribute("checkout", checkout);
        model.addAttribute("qtdHospede", qtdHospede);
        model.addAttribute("dias", dias);
        model.addAttribute("total", total);

        return "reservas/confirmar";
    }


    @PostMapping("/reservar/{id}")
    public String reservar(@PathVariable Long id,
                           @RequestParam LocalDate checkin,
                           @RequestParam LocalDate checkout,
                           @RequestParam Integer qtdHospede,
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

        long dias = ChronoUnit.DAYS.between(checkin, checkout);
        BigDecimal total = imovel.getPrecoDiaria()
                .multiply(BigDecimal.valueOf(dias));

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setImovel(imovel);
        reserva.setCheckin(checkin);
        reserva.setCheckout(checkout);
        reserva.setQtdHospedes(qtdHospede);
        reserva.setTotal(total);

        reservaService.salvar(reserva);

        return "/usuarios/inicio";
    }
    @GetMapping("/listarMet")
    public String listarMet(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("método", metPagService.listar()  );

        return "/metodoPag/listar";

    }

    @GetMapping("/addMetodo")
    public String addMetodo(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("metodo", new MetodoPagamento());

        return "/metodoPag/criar";
    }

    @PostMapping("/salvarMetodo")
    public String salvar(
            @ModelAttribute MetodoPagamento metodoPagamento,
            @RequestParam("arquivo") MultipartFile file,
            Model model) throws IOException {

        if (!file.isEmpty()) {
            String titulo = file.getOriginalFilename();
            Path caminho = Paths.get("src/main/resources/static/uploads/metodo_pag/" + titulo);
            Files.copy(file.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);

            metodoPagamento.setCaminho("metodo_pag/" + titulo);
        }

        if (metPagRepository.existsByTituloIgnoreCase(metodoPagamento.getTitulo())) {
            model.addAttribute("erro", "Já existe um método com este nome");
            return "metodoPag/criar";
        }

        try {
            metPagService.salvar(metodoPagamento);
            return "redirect:/usuarios/addMetodo";
        } catch (Exception e) {
            model.addAttribute("erro", "Ocorreu um erro do nosso lado, tente novamente mais tarde");
            return "metodoPag/criar";
        }
    }



}