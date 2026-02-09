package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.TipoLugar;
import com.bemystay.be_my_stay.repository.TLugarRepository;
import com.bemystay.be_my_stay.service.TLugarService;
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
@RequestMapping("/tipolugar")
public class TLugarController {
    private final TLugarService tLugarService;
    private final TLugarRepository tLugarRepository;

    public TLugarController(TLugarService tLugarService, TLugarRepository tLugarRepository) {
        this.tLugarService = tLugarService;
        this.tLugarRepository = tLugarRepository;
    }


    @GetMapping("/listar")
    public String listar(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("tipo_lugar", tLugarService.listar());
        model.addAttribute("contarTotal", tLugarService.contarTotal());
        model.addAttribute("ativos", tLugarService.contarAtivos());
        model.addAttribute("inativos", tLugarService.contarInativos());
        return "lugar/TLugar";
    }

    @GetMapping("/criar")
    public String criar(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("tipo_lugar", new TipoLugar());

        return "lugar/adicionar";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute TipoLugar tipoLugar, @RequestParam("arquivo") MultipartFile file, Model model) throws IOException {

        if (!file.isEmpty()) {
            String nome = file.getOriginalFilename();
            Path caminho = Paths.get(
                    "src/main/resources/static/uploads/tipo_lugar/" + nome
            );
            Files.copy(file.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);
            tipoLugar.setIcone("tipo_lugar/" + nome);
        }

        if  (tLugarRepository.existsByNomeIgnoreCase(tipoLugar.getTitulo())) {
            model.addAttribute("erro", "Já existe um tipo de lugar com este título");

            return "lugar/adicionar";

        }
        tLugarService.salvar(tipoLugar);

        return "redirect:/tipolugar/listar";

    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        TipoLugar l = tLugarService.buscarPorId(id);
        model.addAttribute("tipo_lugar", l);
        return "lugar/editar";
    }

    @PostMapping("/salvarEdicao/{id}")
    public String salvarEdicao(@PathVariable Long id, @ModelAttribute TipoLugar tipoLugar) {
        tLugarService.editar(id, tipoLugar);
        return "redirect:/tipolugar/listar";

    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        tLugarService.desativar(id);
        return "redirect:/tipolugar/listar";
    }

    @GetMapping("/listarInativas")
    public String listarInativas(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("tipo_lugar", tLugarService.listarInativas());
        return "lugar/restaurar";
    }

    @PostMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id) {
        tLugarService.ativar(id);
        return "redirect:/tipolugar/listar";
    }

}
