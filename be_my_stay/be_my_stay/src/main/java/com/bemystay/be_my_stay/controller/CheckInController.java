package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.CheckIn;

import com.bemystay.be_my_stay.repository.CheckInRepository;
import com.bemystay.be_my_stay.service.CheckInService;
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
public class CheckInController {
    private final CheckInService checkInService;
    private final CheckInRepository checkInRepository;

    public CheckInController(CheckInService checkInService, CheckInRepository checkInRepository) {
        this.checkInService = checkInService;
        this.checkInRepository = checkInRepository;
    }
    @GetMapping("/criar")
    public String criar(HttpSession session, Model model ) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("checkin", new CheckIn());
        return "checkin/adicionar";

    }
    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute CheckIn checkIn,
            @RequestParam("arquivo") MultipartFile file
            , Model model ) throws IOException {

        if (!file.isEmpty()) {
            String nome = file.getOriginalFilename();
            Path caminho = Paths.get(
                    "src/main/resources/static/uploads/checkin/" + nome
            );
            Files.copy(file.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);
            checkIn.setIcone("checkin/" + nome);
        }
        if  (checkInRepository.existsByTituloIgnoreCase(checkIn.getTitulo())) {
            model.addAttribute("erro", "Já existe um método com este nome");

            return "checkin/adicionar";
        }
        try {
            checkInService.salvar(checkIn);
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
        model.addAttribute("checkin", checkInService.listar());
        model.addAttribute("contarTotal", checkInService.contarTotal());
        model.addAttribute("inativos", checkInService.contarInativos());
        model.addAttribute("ativos", checkInService.contarAtivos());

        return "checkin/listar";
    }

    @GetMapping("/listarInativas")
    public String listarInativas(HttpSession session,  Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("checkin", checkInService.listarInativas());
        return "checkin/restaurar";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model){
        CheckIn c = checkInService.buscarPorId(id);
        model.addAttribute("checkin", c);
        return "checkin/editar";

    }

    @PostMapping("/salvarEdicao/{id}")
    public String salvarEdicao(@PathVariable Long id,@ModelAttribute CheckIn checkIn){
        checkInService.editar(id, checkIn);
        return "redirect:/checkin/listar";
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        checkInService.desativar(id);
        return "redirect:/checkin/listar";
    }

    @PostMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id){
        checkInService.ativar(id);
        return "redirect:/checkin/listar";
    }
}
