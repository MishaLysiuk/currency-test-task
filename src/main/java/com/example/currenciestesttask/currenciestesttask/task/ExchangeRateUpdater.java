package com.example.currenciestesttask.currenciestesttask.task;

import com.example.currenciestesttask.currenciestesttask.entity.Currency;
import com.example.currenciestesttask.currenciestesttask.repository.CurrencyExchangeRateRepository;
import com.example.currenciestesttask.currenciestesttask.response.CurrencyResponseModel;
import com.example.currenciestesttask.currenciestesttask.service.CurrencyService;
import com.example.currenciestesttask.currenciestesttask.service.CurrencyTypeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ExchangeRateUpdater {

    private final CurrencyService currencyService;
    private final CurrencyExchangeRateRepository exchangeRateRepository;
    private final CurrencyTypeService currencyTypeService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${exchangeratesapi.accesskey}")
    private String ACCESS_KEY;

    private final String CURRENCY_API_TEMPLATE = "https://api.exchangeratesapi.io/v1/latest?access_key=";

    @Autowired
    public ExchangeRateUpdater(
        CurrencyService currencyService,
        CurrencyExchangeRateRepository exchangeRateRepository,
        CurrencyTypeService currencyTypeService,
        RestTemplate restTemplate,
        ObjectMapper objectMapper) {
        this.currencyService = currencyService;
        this.exchangeRateRepository = exchangeRateRepository;
        this.currencyTypeService = currencyTypeService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedRateString ="${scheduled.task.updateRates.timeInMs}")
    public void updateRates() {
        List<Currency> currencies = currencyService.getCurrencies();
        currencies.forEach(currency -> {
            String response = restTemplate.getForObject(CURRENCY_API_TEMPLATE + ACCESS_KEY + "&base=" + currency.getCurrencyType().getCode(), String.class);
            try {
                CurrencyResponseModel currencyResponse = objectMapper.readValue(response, CurrencyResponseModel.class);
                updateRate(currencyResponse, currency);
                currencyService.save(currency);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void updateRate(CurrencyResponseModel updatedValue, Currency oldValue) {
        oldValue.getExchangeRates().forEach(
            rate -> rate.setRate(updatedValue.getRates().get(rate.getCurrencyTarget().getCode()))
        );
    }
}
