package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.Comodidade;
import com.bemystay.be_my_stay.repository.ComodidadeRepository;
import com.bemystay.be_my_stay.service.ComodidadeService;
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
@RequestMapping("/comodidades")
public class ComodidadeController {
    private final ComodidadeService service;

    public ComodidadeController(ComodidadeService service) {
        this.service = service;
    }

    @GetMapping("/criar")
    public String criar(HttpSession session, Model model ) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        model.addAttribute("comodidade", new Comodidade());
        return "comodidades/addComodidades";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute Comodidade comodidade,
            @RequestParam("arquivo") MultipartFile file
    ) throws IOException {

        if (!file.isEmpty()) {
            String nome = file.getOriginalFilename();
            Path caminho = Paths.get(
                    "src/main/resources/static/uploads/comodidades/" + nome
            );
            Files.copy(file.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);
            comodidade.setIcone("comodidades/" + nome);
        }

        service.salvar(comodidade);
        return "redirect:/comodidades/listar";

    }
    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        service.desativar(id);
        return "redirect:/comodidades/listar";
    }
    @PostMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id){
        service.ativar(id);
        return "redirect:/comodidades/listar";
    }

    @GetMapping("/listar")
    public String listar(HttpSession session,  Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("comodidades", service.listar());
        model.addAttribute("contarTotal", service.contarTotal());
        model.addAttribute("ativas", service.contarAtivas());
        model.addAttribute("inativas", service.contarInativas());
        return "comodidades/comodidades";
    }


    @GetMapping("/listarInativas")
    public String listarInativas(HttpSession session,  Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("comodidades", service.listarInativas());
        return "comodidades/restaurarComodidades";
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Comodidade c = service.buscarPorId(id);
        model.addAttribute("comodidade", c);
        return "comodidades/editar";
    }

    @PostMapping("/salvarEdicao/{id}")
    public String salvarEdicao(@PathVariable Long id,@ModelAttribute Comodidade comodidade) {

        service.editar(id, comodidade);
        return "redirect:/comodidades/listar";
    }



}
