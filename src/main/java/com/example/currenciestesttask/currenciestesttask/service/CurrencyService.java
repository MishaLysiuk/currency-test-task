package com.example.currenciestesttask.currenciestesttask.service;

import com.example.currenciestesttask.currenciestesttask.dto.CurrencyDto;
import com.example.currenciestesttask.currenciestesttask.entity.Currency;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface CurrencyService {
    List<CurrencyDto> getCurrencies();

    void addCurrency(String code) throws JsonProcessingException;

    void save(Currency currency);

    boolean currencyAlreadyAdded(String code);

    Currency getCurrencyByCode(String code);
}
