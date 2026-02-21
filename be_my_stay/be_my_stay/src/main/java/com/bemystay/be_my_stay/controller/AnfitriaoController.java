package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.Comodidade;
import com.bemystay.be_my_stay.model.Imovel;
import com.bemystay.be_my_stay.model.Usuario;
import com.bemystay.be_my_stay.repository.ImovelRepository;
import com.bemystay.be_my_stay.service.ComodidadeService;
import com.bemystay.be_my_stay.service.ImovelService;
import com.bemystay.be_my_stay.service.UsuarioService;
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
    private final UsuarioService usuarioService;

    public AnfitriaoController(ImovelService imovelService, ImovelRepository imovelRepository, ComodidadeService comodidadeService, UsuarioService usuarioService) {
        this.imovelService = imovelService;
        this.imovelRepository = imovelRepository;
        this.comodidadeService = comodidadeService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/inicio")
    public String anfitriao(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        List<Imovel> imoveis = imovelRepository.findByUsuarioIdAndAtivoTrue(idUsuario);
        model.addAttribute("imoveis", imoveis);
        model.addAttribute("usuarioLogado", usuario);

        return "anfitriao/anfitriao";
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
        List<Comodidade> comodidades= comodidadeService.listar();
        model.addAttribute("imovel", i);
        model.addAttribute("comodidades", comodidades);
        return "anfitriao/editarImovel";
    }
    @PostMapping("/salvarEdicao/{id}")
    public String salvarEdicao(@PathVariable Long id, @ModelAttribute Imovel imovel) {
        imovelService.editar(id, imovel);
        return "redirect:/anfitriao/listarImoveis";

    }

}
