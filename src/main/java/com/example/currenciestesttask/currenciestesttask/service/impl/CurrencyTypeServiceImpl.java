package com.example.currenciestesttask.currenciestesttask.service.impl;

import com.example.currenciestesttask.currenciestesttask.entity.CurrencyType;
import com.example.currenciestesttask.currenciestesttask.repository.CurrencyTypeRepository;
import com.example.currenciestesttask.currenciestesttask.service.CurrencyTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyTypeServiceImpl implements CurrencyTypeService {

    private final CurrencyTypeRepository currencyTypeRepository;

    public CurrencyTypeServiceImpl(CurrencyTypeRepository currencyTypeRepository) {
        this.currencyTypeRepository = currencyTypeRepository;
    }

    @Override
    public List<CurrencyType> getCurrencyTypes() {
        return currencyTypeRepository.findAll();
    }

    @Override
    public CurrencyType getCurrencyTypeByCode(String code) {
        return currencyTypeRepository.findByCode(code);
    }

    @Override
    public void saveAll(List<CurrencyType> currencyTypes) {
        currencyTypeRepository.saveAll(currencyTypes);
    }

    @Override
    public List<CurrencyType> findByCodeIn(List<String> codes) {
        return currencyTypeRepository.findByCodeIn(codes);
    }
}
