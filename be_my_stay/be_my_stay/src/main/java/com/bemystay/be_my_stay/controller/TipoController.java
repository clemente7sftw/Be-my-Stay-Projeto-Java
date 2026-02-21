package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.TipoImovel;
import com.bemystay.be_my_stay.repository.TipoRepository;
import com.bemystay.be_my_stay.service.TipoService;
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
@RequestMapping("/tiposImoveis")
public class TipoController {
    private final TipoService tipoService;
    private final TipoRepository tipoRepository;

    public TipoController(TipoService tipoService, TipoRepository tipoRepository) {
        this.tipoService = tipoService;
        this.tipoRepository = tipoRepository;
    }

    @GetMapping("/criar")
    public String criar(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("tipo_imovel", new TipoImovel());
        return "/tipos/adicionar";

    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute TipoImovel tipoImovel, @RequestParam("arquivo") MultipartFile file, Model model) throws IOException {

        if (!file.isEmpty()) {
            String nome = file.getOriginalFilename();
            Path caminho = Paths.get(
                    "src/main/resources/static/uploads/tipos_imoveis/" + nome
            );
            Files.copy(file.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);
            tipoImovel.setIcone("tipos_imoveis/" + nome);
        }
        if  (tipoRepository.existsByNomeIgnoreCase(tipoImovel.getNome())) {
            model.addAttribute("erro", "Já existe um tipo de imóvel com este nome");

            return "adicionar";
        }

        tipoService.salvar(tipoImovel);

        return "redirect:/tiposImoveis/listar";

    }

    @GetMapping("/listar")
    public String listar(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("tipo_imovel", tipoService.listar());
        model.addAttribute("contarTotal", tipoService.contarTotal());
        model.addAttribute("inativos", tipoService.contarInativos());
        model.addAttribute("ativos", tipoService.contarAtivos());

        return "tipos/tiposImoveis";
    }


    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        TipoImovel t = tipoService.buscarPorId(id);
        model.addAttribute("tipo_imovel", t);
        return "tipos/editar";

    }

    @PostMapping("/salvarEdicao/{id}")
    public String salvarEdicao(@PathVariable Long id, @ModelAttribute TipoImovel tipoImovel) {
        tipoService.editar(id, tipoImovel);
        return "redirect:/tiposImoveis/listar";
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        tipoService.desativar(id);
        return "redirect:/tiposImoveis/listar";
    }

    @PostMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id) {
        tipoService.ativar(id);
        return "redirect:/tiposImoveis/listar";
    }

    @GetMapping("/listarInativas")
    public String listarInativas(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("tipo_imovel", tipoService.listarInativas());
        return "tipos/restaurar";
    }

}
