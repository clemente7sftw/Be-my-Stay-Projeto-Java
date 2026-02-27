package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.EntregaChaves;

import com.bemystay.be_my_stay.repository.EntregaChavesRepository;
import com.bemystay.be_my_stay.service.EntregaChavesService;
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
@RequestMapping("/checkin")
public class EntregaChavesController {
    private final EntregaChavesService entregaChavesService;
    private final EntregaChavesRepository entregaChavesRepository;

    public EntregaChavesController(EntregaChavesService entregaChavesService, EntregaChavesRepository entregaChavesRepository) {
        this.entregaChavesService = entregaChavesService;
        this.entregaChavesRepository = entregaChavesRepository;
    }
    @GetMapping("/criar")
    public String criar(HttpSession session, Model model ) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("chaves", new EntregaChaves());
        return "checkin/adicionar";

    }
    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute EntregaChaves entregaChaves,
            @RequestParam("arquivo") MultipartFile file
            , Model model ) throws IOException {

        if (!file.isEmpty()) {
            String nome = file.getOriginalFilename();
            Path caminho = Paths.get(
                    "src/main/resources/static/uploads/chaves/" + nome
            );
            Files.copy(file.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);
            entregaChaves.setIcone("chaves/" + nome);
        }
        if  (entregaChavesRepository.existsByTituloIgnoreCase(entregaChaves.getTitulo())) {
            model.addAttribute("erro", "Já existe um método com este nome");

            return "checkin/adicionar";
        }
        try {
            entregaChavesService.salvar(entregaChaves);
            return "redirect:/checkin/listar";

        } catch (Exception e) {
            model.addAttribute("erro", "Ocorreu um erro do nosso lado, tente novamente mais tarde");
            return "checkin/adicionar";
        }

    }
    @GetMapping("/listar")
    public String listar(HttpSession session,  Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("chaves", entregaChavesService.listar());
        model.addAttribute("contarTotal", entregaChavesService.contarTotal());
        model.addAttribute("inativos", entregaChavesService.contarInativos());
        model.addAttribute("ativos", entregaChavesService.contarAtivos());

        return "checkin/listar";
    }

    @GetMapping("/listarInativas")
    public String listarInativas(HttpSession session,  Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("chaves", entregaChavesService.listarInativas());
        return "checkin/restaurar";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model){
        EntregaChaves c = entregaChavesService.buscarPorId(id);
        model.addAttribute("chaves", c);
        return "checkin/editar";

    }

    @PostMapping("/salvarEdicao/{id}")
    public String salvarEdicao(@PathVariable Long id,@ModelAttribute EntregaChaves entregaChaves){
        entregaChavesService.editar(id, entregaChaves);
        return "redirect:/checkin/listar";
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        entregaChavesService.desativar(id);
        return "redirect:/checkin/listar";
    }

    @PostMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id){
        entregaChavesService.ativar(id);
        return "redirect:/checkin/listar";
    }
}
