package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.Comodidade;
import com.bemystay.be_my_stay.model.MetodoPagamento;
import com.bemystay.be_my_stay.repository.MetPagRepository;
import com.bemystay.be_my_stay.service.MetPagService;
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
@RequestMapping("/metodo")
public class MetodoController {
    private final MetPagService metPagService;
    private final MetPagRepository metPagRepository;

    public MetodoController(MetPagService metPagService, MetPagRepository metPagRepository) {
        this.metPagService = metPagService;
        this.metPagRepository = metPagRepository;
    }

    @GetMapping("/criar")
    public String criar(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("metodo", new MetodoPagamento());

        return "/metodoPag/adicionar";
    }

    @PostMapping("/salvar")
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
            return "metodoPag/adicionar";
        }

        try {
            metPagService.salvar(metodoPagamento);
            return "redirect:/usuarios/addMetodo";
        } catch (Exception e) {
            model.addAttribute("erro", "Ocorreu um erro do nosso lado, tente novamente mais tarde");
            return "metodoPag/adicionar";
        }
    }

    @GetMapping("/listar")
    public String listar(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("método", metPagService.listar()  );

        return "/metodoPag/listar";

    }

    @GetMapping("/listarInativos")
    public String listarInativos(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("método", metPagService.listarInativos()  );

        return "/metodoPag/restaurar";

    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        MetodoPagamento m = metPagService.buscarPorId(id);
        model.addAttribute("método", m);
        return "metodoPag/editar";
    }

    @PostMapping("/salvarEdicao/{id}")
    public String salvarEdicao(@PathVariable Long id,@ModelAttribute MetodoPagamento metodoPagamento ) {

        metPagService.editar(id, metodoPagamento);
        return "redirect:/usuarios/listarMet";
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        metPagService.desativar(id);
        return "redirect:/usuarios/listarMet";
    }
    @PostMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id){
        metPagService.ativar(id);
        return "redirect:/usuarios/listarMet";
    }

}
