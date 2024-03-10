package com.example.currenciestesttask.currenciestesttask.service;

import com.example.currenciestesttask.currenciestesttask.entity.Currency;

import java.util.List;

public interface CurrencyService {
    List<Currency> getCurrencies();

    void addCurrency(String code);

    void save(Currency currency);

    boolean currencyAlreadyAdded(String code);
}
