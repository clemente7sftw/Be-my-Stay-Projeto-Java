package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.Imovel;
import com.bemystay.be_my_stay.model.TipoImovel;
import com.bemystay.be_my_stay.model.TipoLugar;
import com.bemystay.be_my_stay.service.ComodidadeService;
import com.bemystay.be_my_stay.service.ImovelService;
import com.bemystay.be_my_stay.service.TLugarService;
import com.bemystay.be_my_stay.service.TipoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/imovel")
@SessionAttributes("imovel")
public class ImovelController {
    private final ImovelService imovelService;
    private final ComodidadeService comodidadeService;
    private final TLugarService tLugarService;
    private final TipoService tipoService;

    public ImovelController(ImovelService imovelService, ComodidadeService comodidadeService, TLugarService tLugarService, TipoService tipoService) {
        this.imovelService = imovelService;
        this.comodidadeService = comodidadeService;
        this.tLugarService = tLugarService;
        this.tipoService = tipoService;
    }


    @GetMapping("/criar")
    public String criar(HttpSession session, Model model ) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("imovel", new Imovel());
        model.addAttribute("tipoimovel", tipoService.listar());

        return "imoveis/addTipo";
    }
    @PostMapping("/salvarImovel")
    public String salvarImovel(  @RequestParam Long idTipo,
                                 @ModelAttribute("imovel") Imovel imovel){
        TipoImovel tipoImovel = tipoService.buscarPorId(idTipo);
        imovel.setTipoImovel(tipoImovel);


        return "redirect:/imovel/addLugar";

    }
    @GetMapping("/addLugar")
    public String addTLugar(HttpSession session, Model model ) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("tipolugar", tLugarService.listar());

        return "imoveis/addTLugar";
    }
    @PostMapping("/salvarLugar")
    public String salvarLugar(  @RequestParam Long idTLugar,
                                 @ModelAttribute("imovel") Imovel imovel){
    TipoLugar tipoLugar = tLugarService.buscarPorId(idTLugar);
    imovel.setTipoLugar(tipoLugar);

        return "redirect:/imovel/teste";

    }
}
