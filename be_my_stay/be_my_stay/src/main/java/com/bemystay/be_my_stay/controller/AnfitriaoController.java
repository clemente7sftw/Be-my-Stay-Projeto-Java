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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        model.addAttribute("contarTotal", imovelService.contarTotal());
        model.addAttribute("contarAtivos", imovelService.contarAtivos());
        model.addAttribute("contarInativos", imovelService.contarInativos());

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

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        model.addAttribute("usuario", usuario);


        return "anfitriao/perfil";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario u = usuarioService.buscarPorId(id);
        model.addAttribute("usuario", u);

        return "anfitriao/perfilEdicao";

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


        usuarioService.editar(id, usuario);

        return "redirect:/anfitriao/perfil";
    }




}
