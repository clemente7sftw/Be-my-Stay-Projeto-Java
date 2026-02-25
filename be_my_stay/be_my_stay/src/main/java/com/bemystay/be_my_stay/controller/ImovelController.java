package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.*;
import com.bemystay.be_my_stay.repository.ImovelRepository;
import com.bemystay.be_my_stay.repository.ReservaRepository;
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
import java.time.LocalDate;
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
    private final ReservaService reservaService;
    private final ReservaRepository reservaRepository;
    private final ImovelRepository imovelRepository;


    public ImovelController(ImovelService imovelService, ComodidadeService comodidadeService, TLugarService tLugarService, TipoService tipoService, ReservaService reservaService, ReservaRepository reservaRepository, ImovelRepository imovelRepository) {
        this.imovelService = imovelService;
        this.comodidadeService = comodidadeService;
        this.tLugarService = tLugarService;
        this.tipoService = tipoService;
        this.reservaService = reservaService;
        this.reservaRepository = reservaRepository;
        this.imovelRepository = imovelRepository;
    }

    @ModelAttribute("imovel")
    public Imovel carregarImovel() {
        return new Imovel();
    }

    @GetMapping("/criar")
    public String criar(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("imovel", new Imovel());
        model.addAttribute("tipoimovel", tipoService.listar());

        return "imoveis/addTipo";
    }

    @PostMapping("/salvarTipo")
    public String salvarTipo(@RequestParam Long idTipo,
                               @ModelAttribute("imovel") Imovel imovel) {
        TipoImovel tipoImovel = tipoService.buscarPorId(idTipo);
        imovel.setTipoImovel(tipoImovel);

        return "redirect:/imovel/addLugar";

    }

    @GetMapping("/addLugar")
    public String addTLugar(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("tipolugar", tLugarService.listar());

        return "imoveis/addTLugar";
    }

    @PostMapping("/salvarLugar")
    public String salvarLugar(@RequestParam Long idTLugar,
                              @ModelAttribute("imovel") Imovel imovel) {
        TipoLugar tipoLugar = tLugarService.buscarPorId(idTLugar);
        imovel.setTipoLugar(tipoLugar);

        return "redirect:/imovel/comodidades";

    }

    @GetMapping("/comodidades")
    public String comodidades(HttpSession session, Model model) {
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
    public String info(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        Imovel imovel = new Imovel();
        imovel.setEndereco(new Endereco());
        return "imoveis/addInfo";
    }

    @PostMapping("/salvarInfo")
    public String salvarInfo(@ModelAttribute("imovel") Imovel imovel) {
        return "redirect:/imovel/titulo";
    }

    @GetMapping("/titulo")
    public String titulo(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        return "imoveis/addTitulo";
    }

    @PostMapping("/salvarTitulo")
    public String salvarTitulo(@ModelAttribute("imovel") Imovel imovel) {
        return "redirect:/imovel/fotos";

    }

    @GetMapping("/fotos")
    public String fotos(HttpSession session, Model model) {
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

                fotosTemp.add("imoveis/" + nomeArquivo);
            }
        }

        session.setAttribute("fotosImovel", fotosTemp);

        return "redirect:/imovel/descricao";
    }

    @GetMapping("/descricao")
    public String descricao(HttpSession session, Model model) {
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

    @PostMapping("/salvarImovel")
    public String salvarImovel(
            HttpSession session,
            @ModelAttribute("imovel") Imovel imovel,
            SessionStatus status, Model model
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


        try {
            imovelService.salvar(imovel);

            status.setComplete();

            return "/imoveis/redirecionamento";

        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("imovel", imovel);
            return "imoveis/addDescricao";
        }
    }

    @GetMapping("/listarAdmin")
    public String listarAdmin(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("imovel", imovelService.listar());
        model.addAttribute("contarTotal", imovelService.contarTotal());
        model.addAttribute("ativas", imovelService.contarAtivos());
        model.addAttribute("inativas", imovelService.contarInativos());
        return "admin/imoveis";
    }

    @GetMapping("/listarInativas")
    public String listarInativas(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("imovel", imovelService.listarInativas());
        return "imoveis/restaurarAdmin";
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        imovelService.desativar(id);
        return "redirect:/imovel/listarAdmin";
    }

    @PostMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id) {
        imovelService.ativar(id);
        return "redirect:/imovel/listarAdmin";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Imovel i = imovelService.buscarPorId(id);
        model.addAttribute("imovel", i);
        return "imoveis/editar";
    }

    //hospedes


    @GetMapping("/mostrarDescricao/{id}")
    public String mostrarDescricao(@PathVariable Long id, Model model) {
        Imovel imovel = imovelService.buscarPorId(id);
        List<Reserva> reservas = reservaService.buscarAtivasPorImovel(id);

        List<String> datasBloqueadas = new ArrayList<>();

        for (Reserva r : reservas) {
            LocalDate data = r.getCheckin();
            while (!data.isAfter(r.getCheckout().minusDays(1))) {
                datasBloqueadas.add(data.toString());
                data = data.plusDays(1);
            }
        }
        model.addAttribute("hoje", LocalDate.now());
        model.addAttribute("datasBloqueadas", datasBloqueadas);
        model.addAttribute("imovel", imovel);
        model.addAttribute("comodidades", comodidadeService.listar());
        model.addAttribute("imagens", imovel.getImagens());
        return "imoveis/descricao";
    }

    @GetMapping("/listarHospede")
    public String listar(HttpSession session, Model model, @RequestParam(required = false) Long tipo) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        List<Imovel> imoveis;

        if (tipo != null) {
            imoveis = imovelService.listarAtivosPorTipo(tipo);
        } else {
            imoveis = imovelService.listar();
        }

        model.addAttribute("imoveis", imoveis);
        model.addAttribute("tipo_imovel", tipoService.listar());

        return "usuarios/inicio";
    }


    @GetMapping("/buscar")
    public String buscar(@RequestParam("cidade") String cidade, Model model) {

        List<Imovel> imoveis =
                imovelRepository.findByEndereco_CidadeContainingIgnoreCaseAndAtivoTrue(cidade);


        if (imoveis.isEmpty()) {
            model.addAttribute("erro", "Nenhum imóvel encontrado para esse local.");
        }

        model.addAttribute("imoveis", imoveis);
        model.addAttribute("localPesquisado", cidade);

        return "usuarios/inicio";
    }

    //anfitriao
    @GetMapping("/listarInativasAnfitriao")
    public String listarInativasAnfitriao(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("imovel", imovelService.listarInativas());
        return "anfitriao/restaurarImoveis";
    }
    @PostMapping("/deletarAnfitriao/{id}")
    public String deletarAnfitriao(@PathVariable Long id) {
        imovelService.desativar(id);
        return "redirect:/imovel/listarAnfitriao";
    }

    @PostMapping("/restaurarAnfitriao/{id}")
    public String restaurarAnfitriao(@PathVariable Long id) {
        imovelService.ativar(id);
        return "redirect:/imovel/listarAnfitriao";
    }
    @GetMapping("/editarAnfitriao/{id}")
    public String editarAnfitriao(@PathVariable Long id, Model model) {
        Imovel i = imovelService.buscarPorId(id);
        List<Comodidade> comodidades= comodidadeService.listar();
        model.addAttribute("imovel", i);
        model.addAttribute("comodidades", comodidades);
        return "anfitriao/editarImovel";
    }
    @PostMapping("/salvarEdicaoAnfitriao/{id}")
    public String salvarEdicaoAnfitriao(@PathVariable Long id, @ModelAttribute Imovel imovel) {
        imovelService.editar(id, imovel);
        return "redirect:/imovel/listarAnfitriao";

    }
    @GetMapping("/listarAnfitriao")
    public String listar(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }

        List<Imovel> imoveis = imovelRepository.findByUsuarioIdAndAtivoTrue(idUsuario);
        model.addAttribute("imoveis", imoveis);
        model.addAttribute("contarTotal", imovelService.contarTotal());
        model.addAttribute("contarAtivos", imovelService.contarAtivos());
        model.addAttribute("contarInativos", imovelService.contarInativos());

        return "anfitriao/imoveis";
    }

}
