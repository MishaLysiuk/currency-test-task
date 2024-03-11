package com.example.currenciestesttask.currenciestesttask.service.impl;

import com.example.currenciestesttask.currenciestesttask.dto.CurrencyExchangeRateDto;
import com.example.currenciestesttask.currenciestesttask.entity.Currency;
import com.example.currenciestesttask.currenciestesttask.entity.CurrencyExchangeRate;
import com.example.currenciestesttask.currenciestesttask.repository.CurrencyExchangeRateRepository;
import com.example.currenciestesttask.currenciestesttask.repository.CurrencyRepository;
import com.example.currenciestesttask.currenciestesttask.response.ExchangeRatesResponseModel;
import com.example.currenciestesttask.currenciestesttask.service.CurrencyExchangeRateService;
import com.example.currenciestesttask.currenciestesttask.service.ExchangeRatesApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CurrencyExchangeRateServiceImpl implements CurrencyExchangeRateService {

    private final CurrencyExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;

    private final ExchangeRatesApiService exchangeRatesApiService;

    @Autowired
    public CurrencyExchangeRateServiceImpl(
        CurrencyExchangeRateRepository exchangeRateRepository,
        CurrencyRepository currencyRepository,
        ExchangeRatesApiService exchangeRatesApiService
    ) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.currencyRepository = currencyRepository;
        this.exchangeRatesApiService = exchangeRatesApiService;
    }

    @Override
    public void save(CurrencyExchangeRate currencyExchangeRate) {
        exchangeRateRepository.save(currencyExchangeRate);
    }

    @Override
    public void saveAll(List<CurrencyExchangeRate> currencyExchangeRates) {
        exchangeRateRepository.saveAll(currencyExchangeRates);
    }

    @Override
    public List<CurrencyExchangeRate> findByCurrencySourceAndCurrencyTargetIn(
        Currency currencySource,
        List<Currency> currencyTargets
    ) {
        return exchangeRateRepository.findByCurrencySourceAndCurrencyTargetIn(currencySource, currencyTargets);
    }

    @Override
    @CacheEvict(value = "exchangeRates", allEntries = true)
    public void updateRates() {
        List<Currency> currencies = currencyRepository.findAll();
        List<String> currencyCodes = currencies.stream().map(Currency::getCode).toList();
        List<CurrencyExchangeRate> exchangeRates = exchangeRateRepository.findAll();
        currencyCodes.forEach(code -> {

            List<Currency> filteredCurrencyList = filterCurrency(currencies, code);
            ExchangeRatesResponseModel currencyResponse = exchangeRatesApiService.getExchangeRates(
                code,
                filteredCurrencyList.stream().map(Currency::getCode).toList()
            );
            List<CurrencyExchangeRate> currentCurrencyRates = exchangeRates.stream()
                .filter(rate -> rate.getCurrencySource().getCode().equals(code))
                .toList();
            filteredCurrencyList.forEach(currentCurrency -> {
                Optional<CurrencyExchangeRate> existingRate = currentCurrencyRates.stream()
                    .filter(rate -> rate.getCurrencySource().getCode().equals(code) && rate.getCurrencyTarget()
                        .getCode()
                        .equals(currentCurrency.getCode()))
                    .findFirst();
                if (existingRate.isPresent()) {
                    existingRate.get().setRate(currencyResponse.getRates().get(currentCurrency.getCode()));
                } else {
                    CurrencyExchangeRate newRate = new CurrencyExchangeRate();
                    Currency currencySource = currencies.stream()
                        .filter(source -> source.getCode().equals(code))
                        .findFirst()
                        .get();
                    newRate.setCurrencySource(currencySource);
                    newRate.setCurrencyTarget(currentCurrency);
                    newRate.setRate(currencyResponse.getRates().get(currentCurrency.getCode()));
                    exchangeRates.add(newRate);
                }
            });
        });
        exchangeRateRepository.saveAll(exchangeRates);
    }

    private List<Currency> filterCurrency(List<Currency> currencies, String code) {
        return currencies.stream().filter(currency -> !currency.getCode().equals(code)).toList();
    }

    @Override
    @Cacheable("exchangeRates")
    public CurrencyExchangeRateDto getCurrencyWithRates(String code) {
        CurrencyExchangeRateDto exchangeRateDto = new CurrencyExchangeRateDto();
        List<CurrencyExchangeRate> exchangeRates = exchangeRateRepository.findByCurrencySourceCode(code);
        exchangeRateDto.setSourceCode(code);
        exchangeRateDto.setRates(createRatesMap(exchangeRates));
        return exchangeRateDto;
    }

    private Map<String, Double> createRatesMap(List<CurrencyExchangeRate> exchangeRates) {
        Map<String, Double> ratesMap = new HashMap<>();
        exchangeRates.forEach(rate -> ratesMap.put(rate.getCurrencyTarget().getCode(), rate.getRate()));
        return ratesMap;
    }
}
