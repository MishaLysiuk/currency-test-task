package com.example.currenciestesttask.currenciestesttask.service.impl;

import com.example.currenciestesttask.currenciestesttask.dto.CurrencyDto;
import com.example.currenciestesttask.currenciestesttask.entity.Currency;
import com.example.currenciestesttask.currenciestesttask.repository.CurrencyRepository;
import com.example.currenciestesttask.currenciestesttask.service.CurrencyExchangeRateService;
import com.example.currenciestesttask.currenciestesttask.service.CurrencyService;
import com.example.currenciestesttask.currenciestesttask.service.ExchangeRatesApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyExchangeRateService exchangeRateService;
    private final ExchangeRatesApiService exchangeRatesApiService;

    @Autowired
    public CurrencyServiceImpl(
        CurrencyRepository currencyRepository,
        CurrencyExchangeRateService exchangeRateService,
        ExchangeRatesApiService exchangeRatesApiService
    ) {
        this.currencyRepository = currencyRepository;
        this.exchangeRateService = exchangeRateService;
        this.exchangeRatesApiService = exchangeRatesApiService;
    }

    @Override
    public List<CurrencyDto> getCurrencies() {
        List <Currency> currencies = currencyRepository.findAll();
        return currencies.stream().map(currency -> {
            CurrencyDto currencyDto = new CurrencyDto();
            currencyDto.setCurrencyCode(currency.getCode());
            return currencyDto;
        }).toList();
    }

    @Override
    public void save(Currency currency) {
        currencyRepository.save(currency);
    }

    @Override
    public boolean currencyAlreadyAdded(String code) {
        return getCurrencyByCode(code) != null;
    }

    @Override
    public Currency getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code);
    }

    @Override
    public void addCurrency(String code) {
        if (!isValidCurrencyCode(code)) {
            throw new RuntimeException();
        }
        Currency newCurrency = new Currency();
        newCurrency.setCode(code);
        currencyRepository.save(newCurrency);
        exchangeRateService.updateRates();
    }

    private Boolean isValidCurrencyCode(String code) {
        return exchangeRatesApiService.getSupportedCurrencies().getSymbols().containsKey(code);
    }
}
