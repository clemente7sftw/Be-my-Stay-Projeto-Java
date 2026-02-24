package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.model.Reserva;
import com.bemystay.be_my_stay.repository.ReservaRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping ("/relatorios")

public class RelatorioController {
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private ReservaRepository reservaRepository;
    @GetMapping("/relatoriosAdmin")
    public String relatoriosAdmin(HttpSession session, Model model) {
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/usuarios/login";
        }
        return "relatorios/admin/listar";
    }


    @GetMapping("/reservasTotal")
    public void gerarRelatorio(HttpServletResponse response) throws Exception {

        List<Reserva> reservas = reservaRepository.findAll();

        Context context = new Context();
        context.setVariable("reservas", reservas);
        context.setVariable("total", reservas.size());

        String html = templateEngine.process("relatorios/admin/reservasTotal", context);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=reservas_bemystay.pdf");

        OutputStream outputStream = response.getOutputStream();

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null);
        builder.toStream(outputStream);
        builder.run();

        outputStream.close();
    }

    @GetMapping("/imovelLucrativo")
    public void gerarRelatorioLucrativos(HttpServletResponse response) throws Exception {

        List<Object[]> lista =
                reservaRepository.buscarImoveisMaisLucrativos(LocalDate.now());

        Context context = new Context();
        context.setVariable("imoveis", lista);

        String html = templateEngine.process(
                "relatorios/admin/imoveisLucrativos", context);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=imoveisLucrativos_bemystay.pdf");

        OutputStream os = response.getOutputStream();

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null);
        builder.toStream(os);
        builder.run();

        os.close();
    }
}
