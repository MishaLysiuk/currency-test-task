package com.example.currenciestesttask.currenciestesttask.service;

import com.example.currenciestesttask.currenciestesttask.dto.CurrencyExchangeRateDto;
import com.example.currenciestesttask.currenciestesttask.entity.Currency;
import com.example.currenciestesttask.currenciestesttask.entity.CurrencyExchangeRate;

import java.util.List;

public interface CurrencyExchangeRateService {

    void save(CurrencyExchangeRate currencyExchangeRate);

    void saveAll(List<CurrencyExchangeRate> currencyExchangeRates);

    List<CurrencyExchangeRate> findByCurrencySourceAndCurrencyTargetIn(Currency currencySource, List<Currency> currencyTargets);

    void updateRates();

    CurrencyExchangeRateDto getCurrencyWithRates(String code);
}
