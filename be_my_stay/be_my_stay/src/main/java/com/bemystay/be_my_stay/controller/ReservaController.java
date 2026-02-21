package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.Imovel;
import com.bemystay.be_my_stay.model.MetodoPagamento;
import com.bemystay.be_my_stay.model.Reserva;
import com.bemystay.be_my_stay.model.Usuario;
import com.bemystay.be_my_stay.repository.ReservaRepository;
import com.bemystay.be_my_stay.service.ImovelService;
import com.bemystay.be_my_stay.service.MetPagService;
import com.bemystay.be_my_stay.service.ReservaService;
import com.bemystay.be_my_stay.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservaController {
    private final ReservaService reservaService;
    private final ImovelService imovelService;
    private final MetPagService metPagService;
    private final UsuarioService usuarioService;
    private final ReservaRepository reservaRepository;

    public ReservaController(ReservaService reservaService, ImovelService imovelService, MetPagService metPagService, UsuarioService usuarioService, ReservaRepository reservaRepository) {
        this.reservaService = reservaService;
        this.imovelService = imovelService;
        this.metPagService = metPagService;
        this.usuarioService = usuarioService;
        this.reservaRepository = reservaRepository;
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
        model.addAttribute("método", metPagService.listar());

        return "reservas/confirmar";
    }


    @PostMapping("/reservar/{id}")
    public String reservar(@PathVariable Long id,
                           @RequestParam LocalDate checkin,
                           @RequestParam LocalDate checkout,
                           @RequestParam Integer qtdHospede,
                           @RequestParam ("ids") Long idMetPag,
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


        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        Imovel imovel = imovelService.buscarPorId(id);
        MetodoPagamento metodoPagamento = metPagService.buscarPorId(idMetPag);

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
        reserva.setMetodoPagamento(metodoPagamento);

        reservaService.salvar(reserva);

        return "/usuarios/inicio";
    }

    @GetMapping("/listarHospede")
    public String listarHospede(HttpSession session, Model model) {

        Long idUsuario = (Long) session.getAttribute("idUsuario");

        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        List<Reserva> reservas = reservaRepository.findByUsuarioIdAndAtivoTrue(idUsuario);

        if (reservas.isEmpty()) {
            model.addAttribute("erro", "Você não possui nenhuma reserva");
        } else {
            model.addAttribute("reservas", reservas);
        }

        return "reservas/listar";
    }


    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        reservaService.desativar(id);
        return "redirect:/reservas/listarHospede";
    }
    @PostMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id){
        reservaService.ativar(id);
        return "redirect:/comodidades/listar";
    }
}
