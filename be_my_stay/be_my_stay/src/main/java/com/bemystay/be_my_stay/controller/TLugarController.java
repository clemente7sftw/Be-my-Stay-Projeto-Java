package com.bemystay.be_my_stay.controller;

import com.bemystay.be_my_stay.service.TLugarService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tipolugar")
public class TLugarController {
    private final TLugarService tLugarService;

    public TLugarController(TLugarService tLugarService) {
        this.tLugarService = tLugarService;
    }
    @GetMapping("/listar")
    public String listar () {
        return "lugar/TLugar";
    }
}
