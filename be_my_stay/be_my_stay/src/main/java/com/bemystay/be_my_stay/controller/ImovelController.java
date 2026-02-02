package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.*;
import com.bemystay.be_my_stay.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @ModelAttribute("imovel")
    public Imovel carregarImovel() {
        return new Imovel();
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

        return "redirect:/imovel/comodidades";

    }

    @GetMapping("/comodidades")
    public String comodidades(HttpSession session, Model model ) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        model.addAttribute("comodidade", comodidadeService.listar());
        return "imoveis/addComodidade";
    }

    @PostMapping("/salvarComodidade")
    public String salvarComodidade(
            @RequestParam(required = false) List<Long> ids,
            @ModelAttribute("imovel") Imovel imovel
    ) {
        if (ids != null) {
            List<Comodidade> comodidades =
                    comodidadeService.buscarPorIds(ids);

            imovel.getComodidade().addAll(comodidades);
        }

        return "redirect:/imovel/Info";
    }

    @GetMapping("/Info")
        public String info(HttpSession session, Model model ) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        Imovel imovel = new Imovel();
        imovel.setEndereco(new Endereco());
        return "imoveis/addInfo";
    }

        @PostMapping("/salvarInfo")
        public String salvarInfo(@ModelAttribute("imovel") Imovel imovel){
            return "redirect:/imovel/titulo";
        }

    @GetMapping("/titulo")
    public String titulo(HttpSession session, Model model ) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        return "imoveis/addTitulo";
    }
    @PostMapping("/salvarTitulo")
    public String salvarTitulo( @ModelAttribute("imovel") Imovel imovel){
        return "redirect:/imovel/fotos";

    }
    @GetMapping("/fotos")
    public String fotos(HttpSession session, Model model){
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        return "imoveis/addFotos";
    }
    @PostMapping("/salvarFoto")
    public String salvarFoto(
            HttpSession session,
            @RequestParam("imagens") MultipartFile[] arquivos
    ) throws IOException {

        List<String> fotosTemp = new ArrayList<>();

        for (MultipartFile file : arquivos) {

            if (!file.isEmpty()) {

                String nomeArquivo = UUID.randomUUID() + "_" + file.getOriginalFilename();

                Path caminho = Paths.get(
                        "src/main/resources/static/uploads/imoveis/" + nomeArquivo
                );

                Files.copy(file.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);

                fotosTemp.add("uploads/imoveis/" + nomeArquivo);
            }
        }

        session.setAttribute("fotosImovel", fotosTemp);

        return "redirect:/imovel/descricao";
    }

    @GetMapping("/descricao")
    public String descricao(HttpSession session, Model model ) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        return "imoveis/addDescricao";
    }
    @GetMapping("/cep/{cep}")
    @ResponseBody
    public String buscarCep(@PathVariable String cep) {
        return imovelService.buscarCep(cep);
    }

    @PostMapping("/salvarDescricao")
    public String salvarDescricao(
            HttpSession session,
            @ModelAttribute("imovel") Imovel imovel,
            SessionStatus status
    ) {

        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }


        List<String> fotosTemp =
                (List<String>) session.getAttribute("fotosImovel");

        if (fotosTemp != null) {
            for (String caminho : fotosTemp) {
                Imagem imagem = new Imagem();
                imagem.setCaminho(caminho);
                imagem.setImovel(imovel);
                imovel.getImagens().add(imagem);
            }
        }

        session.removeAttribute("fotosImovel");
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        imovel.setUsuario(usuario);

        imovel.setDataCadastro(LocalDateTime.now());
        imovel.setAtivo(true);

        imovelService.salvar(imovel);

        status.setComplete();

        return "redirect:/imovel/criar";
    }






}
