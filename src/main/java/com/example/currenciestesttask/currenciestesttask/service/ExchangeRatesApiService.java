package com.example.currenciestesttask.currenciestesttask.service;

import com.example.currenciestesttask.currenciestesttask.response.ExchangeRatesResponseModel;
import com.example.currenciestesttask.currenciestesttask.response.SupportedCurrencyResponseModel;

import java.util.List;

public interface ExchangeRatesApiService {
    SupportedCurrencyResponseModel getSupportedCurrencies();

    ExchangeRatesResponseModel getExchangeRates(String sourceCode, List<String> targetCodes);
}
