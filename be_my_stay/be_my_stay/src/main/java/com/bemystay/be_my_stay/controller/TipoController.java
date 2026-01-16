package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.Comodidade;
import com.bemystay.be_my_stay.model.TipoImovel;
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
            public TipoController( TipoService tipoService ) {this.tipoService = tipoService;}

        @GetMapping("/criar")
        public String criar(HttpSession session, Model model ) {
            Long idUsuario = (Long) session.getAttribute("idUsuario");
            if (idUsuario == null) {
                return "redirect:/usuarios/login";
            }
            model.addAttribute("tipo_imovel", new TipoImovel());
            return "tipos/addTipos";

        }
        @PostMapping("/salvar")
        public String salvar (@ModelAttribute TipoImovel tipoImovel,@RequestParam("arquivo") MultipartFile file)throws IOException {

            if (!file.isEmpty()) {
                String nome = file.getOriginalFilename();
                Path caminho = Paths.get(
                        "src/main/resources/static/uploads/tipos_imoveis/" + nome
                );
                Files.copy(file.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);
                tipoImovel.setIcone("tipos_imoveis/" + nome);
            }


            tipoService.salvar(tipoImovel);

            return "redirect:/tiposImoveis/listar";

        }
        @GetMapping("/listar")
        public String listar(HttpSession session,  Model model) {
            Long idUsuario = (Long) session.getAttribute("idUsuario");
            if (idUsuario == null) {
                return "redirect:/usuarios/login";
            }
            model.addAttribute("tipo_imovel", tipoService.listar());
            return "tipos/tiposImoveis";
        }

}
