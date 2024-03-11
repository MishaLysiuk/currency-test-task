package com.example.currenciestesttask.currenciestesttask.repository;

import com.example.currenciestesttask.currenciestesttask.entity.Currency;
import com.example.currenciestesttask.currenciestesttask.entity.CurrencyExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyExchangeRateRepository extends JpaRepository<CurrencyExchangeRate, Long> {

    List<CurrencyExchangeRate> findByCurrencySourceAndCurrencyTargetIn(Currency currencySource, List<Currency> currencyTargets);
    List<CurrencyExchangeRate> findByCurrencySourceCode(String code);
}
