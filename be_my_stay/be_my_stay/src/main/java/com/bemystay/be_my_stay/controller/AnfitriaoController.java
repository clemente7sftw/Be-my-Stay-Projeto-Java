package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.Comodidade;
import com.bemystay.be_my_stay.model.Imovel;
import com.bemystay.be_my_stay.model.TipoLugar;
import com.bemystay.be_my_stay.repository.ImovelRepository;
import com.bemystay.be_my_stay.service.ComodidadeService;
import com.bemystay.be_my_stay.service.ImovelService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/anfitriao")
public class AnfitriaoController {
    private final ImovelService imovelService;
    private final ImovelRepository imovelRepository;
    private final ComodidadeService comodidadeService;

    public AnfitriaoController(ImovelService imovelService, ImovelRepository imovelRepository, ComodidadeService comodidadeService) {
        this.imovelService = imovelService;
        this.imovelRepository = imovelRepository;
        this.comodidadeService = comodidadeService;
    }


    @GetMapping("/listarImoveis")
    public String listarImoveis(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        List<Imovel> imoveis = imovelRepository.findByUsuarioIdAndAtivoTrue(idUsuario);
        model.addAttribute("imoveis", imoveis);

        return "anfitriao/imoveis";
    }
    @GetMapping("/listarInativas")
    public String listarInativas(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        List<Imovel> imoveis = imovelRepository
                .findByUsuarioIdAndAtivoFalse(idUsuario);

        model.addAttribute("imoveis", imoveis);
        return "anfitriao/restaurarImoveis";
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        imovelService.desativar(id);
        return "redirect:/anfitriao/listarImoveis";
    }

    @PostMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id) {
        imovelService.ativar(id);
        return "redirect:/anfitriao/listarImoveis";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Imovel i = imovelService.buscarPorId(id);
        Comodidade c = comodidadeService.buscarPorId(id);
        model.addAttribute("imovel", i);
        model.addAttribute("comodidades", c);
        return "anfitriao/editarImovel";
    }
    @PostMapping("/salvarEdicao/{id}")
    public String salvarEdicao(@PathVariable Long id, @ModelAttribute TipoLugar tipoLugar) {
        imovelService.editar(id, tipoLugar);
        return "redirect:/tipolugar/listar";

    }

}
