package com.example.currenciestesttask.currenciestesttask.service.impl;

import com.example.currenciestesttask.currenciestesttask.entity.CurrencyExchangeRate;
import com.example.currenciestesttask.currenciestesttask.entity.CurrencyType;
import com.example.currenciestesttask.currenciestesttask.entity.Currency;
import com.example.currenciestesttask.currenciestesttask.repository.CurrencyRepository;
import com.example.currenciestesttask.currenciestesttask.response.CurrencyResponseModel;
import com.example.currenciestesttask.currenciestesttask.service.CurrencyExchangeRateService;
import com.example.currenciestesttask.currenciestesttask.service.CurrencyService;
import com.example.currenciestesttask.currenciestesttask.service.CurrencyTypeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyTypeService currencyTypeService;
    private final CurrencyExchangeRateService exchangeRateService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Value("${exchangeratesapi.accesskey}")
    private String ACCESS_KEY;
    private final String CURRENCY_API_TEMPLATE = "https://api.exchangeratesapi.io/v1/latest";

    @Autowired
    public CurrencyServiceImpl(
        CurrencyRepository currencyRepository,
        CurrencyTypeService currencyTypeService,
        CurrencyExchangeRateService exchangeRateService,
        RestTemplate restTemplate,
        ObjectMapper objectMapper
    ) {
        this.currencyRepository = currencyRepository;
        this.currencyTypeService = currencyTypeService;
        this.exchangeRateService = exchangeRateService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Currency> getCurrencies() {
        return currencyRepository.findAll();
    }

    @Override
    public void save(Currency currency) {
        currencyRepository.save(currency);
    }

    @Override
    public boolean currencyAlreadyAdded(String code) {
        return getCurrencyByCode(code) != null;
    }

    public Currency getCurrencyByCode(String code) {
        return currencyRepository.findByCurrencyTypeCode(code);
    }

    @Override
    public void addCurrency(String code) {

        Currency newCurrency = new Currency();
        CurrencyType currencyType = currencyTypeService.getCurrencyTypeByCode(code);
        List<Currency> currencies = currencyRepository.findAll();

        newCurrency.setCurrencyType(currencyType);

        if (!currencies.isEmpty()) {

            String response = restTemplate.getForObject(buildUrlToAddCurrency(code, currencies), String.class);

            try {
                CurrencyResponseModel currencyResponse = objectMapper.readValue(response, CurrencyResponseModel.class);
                List<CurrencyExchangeRate> newRates = createRates(currencyResponse);
                exchangeRateService.saveAll(newRates);
                newCurrency.setExchangeRates(newRates);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        currencyRepository.save(newCurrency);
    }

    private List<CurrencyExchangeRate> createRates(CurrencyResponseModel currencyResponse) {
        CurrencyType base = currencyTypeService.getCurrencyTypeByCode(currencyResponse.getCode());
        List<CurrencyType> currencyTypes = currencyTypeService.findByCodeIn(currencyResponse.getRates()
            .keySet()
            .stream()
            .toList());

        return currencyResponse.getRates().entrySet().stream().map(entry -> {
            CurrencyExchangeRate newRate = new CurrencyExchangeRate();
            newRate.setCurrencySource(base);
            newRate.setCurrencyTarget(getCurrencyTypeByCode(entry.getKey(), currencyTypes).get());
            newRate.setRate(entry.getValue());
            return newRate;
        }).toList();
    }

    private Optional<CurrencyType> getCurrencyTypeByCode(String code, List<CurrencyType> currencyTypes) {
        return currencyTypes.stream().filter(currencyType -> currencyType.getCode().equals(code)).findFirst();
    }

    private String buildUrlToAddCurrency(String code, List<Currency> currencies) {
        return UriComponentsBuilder.fromUriString(CURRENCY_API_TEMPLATE)
            .queryParam("access_key", ACCESS_KEY)
            .queryParam("base", code)
            .queryParam("symbols", convertCurrencyToCodes(currencies))
            .toUriString();
    }

    private String convertCurrencyToCodes(List<Currency> currencies) {
        List<String> currencyCodes = currencies.stream().map(currency -> currency.getCurrencyType().getCode()).toList();
        return String.join(", ", currencyCodes);
    }
}
