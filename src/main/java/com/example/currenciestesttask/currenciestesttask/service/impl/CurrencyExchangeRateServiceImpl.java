package com.example.currenciestesttask.currenciestesttask.service.impl;

import com.example.currenciestesttask.currenciestesttask.entity.Currency;
import com.example.currenciestesttask.currenciestesttask.entity.CurrencyExchangeRate;
import com.example.currenciestesttask.currenciestesttask.entity.CurrencyType;
import com.example.currenciestesttask.currenciestesttask.repository.CurrencyExchangeRateRepository;
import com.example.currenciestesttask.currenciestesttask.repository.CurrencyRepository;
import com.example.currenciestesttask.currenciestesttask.service.CurrencyExchangeRateService;
import com.example.currenciestesttask.currenciestesttask.service.CurrencyTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CurrencyExchangeRateServiceImpl implements CurrencyExchangeRateService {

    private final CurrencyExchangeRateRepository exchangeRateRepository;

    @Autowired
    public CurrencyExchangeRateServiceImpl(CurrencyExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Override
    public void save(CurrencyExchangeRate currencyExchangeRate) {
        exchangeRateRepository.save(currencyExchangeRate);
    }

    @Override
    public void saveAll(List<CurrencyExchangeRate> currencyExchangeRates) {
        exchangeRateRepository.saveAll(currencyExchangeRates);
    }
}
