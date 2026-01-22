package com.bemystay.be_my_stay.controller;
import com.bemystay.be_my_stay.model.TipoImovel;
import com.bemystay.be_my_stay.model.TipoLugar;
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

    public TLugarController(TLugarService tLugarService) {
        this.tLugarService = tLugarService;
    }


    @GetMapping("/listar")
    public String listar(HttpSession session,  Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("tipo_lugar", tLugarService.listar());
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
    public String salvar (@ModelAttribute TipoLugar tipoLugar,@RequestParam("arquivo") MultipartFile file)throws IOException {

        if (!file.isEmpty()) {
            String nome = file.getOriginalFilename();
            Path caminho = Paths.get(
                    "src/main/resources/static/uploads/tipo_lugar/" + nome
            );
            Files.copy(file.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);
            tipoLugar.setIcone("tipo_lugar/" + nome);
        }


        tLugarService.salvar(tipoLugar);

        return "redirect:/tipolugar/listar";

    }

}
