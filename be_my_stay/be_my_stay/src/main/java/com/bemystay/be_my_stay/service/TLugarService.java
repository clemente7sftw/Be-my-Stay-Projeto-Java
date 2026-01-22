package com.bemystay.be_my_stay.service;

import com.bemystay.be_my_stay.repository.TLugarRepository;
import org.springframework.stereotype.Service;

@Service
public class TLugarService {
    private final TLugarRepository tLugarRepository;

    public TLugarService(TLugarRepository tLugarRepository) {
        this.tLugarRepository = tLugarRepository;
    }
}
