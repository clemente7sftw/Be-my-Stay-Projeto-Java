package com.bemystay.be_my_stay.controller;


import com.bemystay.be_my_stay.service.AnfitriaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/anfitriao")
public class AnfitriaoController {
    private final AnfitriaoService anfitriaoService;

    public AnfitriaoController(AnfitriaoService anfitriaoService) {
        this.anfitriaoService = anfitriaoService;
    }
    @GetMapping("/comodidades")
    public String comodidades(HttpSession session, Model model) {

//        Long idUsuario = (Long) session.getAttribute("idUsuario");
//        if (idUsuario == null) {
//            return "redirect:/usuarios/login";
//        }


        return "anfitriao/addComodidades";
    }
}
