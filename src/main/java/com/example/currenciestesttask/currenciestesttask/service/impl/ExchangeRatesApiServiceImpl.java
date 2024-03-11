package com.example.currenciestesttask.currenciestesttask.service.impl;

import com.example.currenciestesttask.currenciestesttask.response.ExchangeRatesResponseModel;
import com.example.currenciestesttask.currenciestesttask.response.SupportedCurrencyResponseModel;
import com.example.currenciestesttask.currenciestesttask.service.ExchangeRatesApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class ExchangeRatesApiServiceImpl implements ExchangeRatesApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ExchangeRatesApiServiceImpl(
        RestTemplate restTemplate,
        ObjectMapper objectMapper
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Value("${exchangeratesapi.accesskey}")
    private String ACCESS_KEY;
    @Value("${exchangeratesapi.url.template}")
    private String API_URL_TEMPLATE;

    private final String CURRENCY_RATES_PATH = "latest";
    private final String CURRENCY_SUPPORTED_PATH = "symbols";

    @Override
    public SupportedCurrencyResponseModel getSupportedCurrencies() {
        try {

            String url = UriComponentsBuilder.fromUriString(API_URL_TEMPLATE)
                .path(CURRENCY_SUPPORTED_PATH)
                .queryParam("access_key", ACCESS_KEY)
                .toUriString();
            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readValue(
                response,
                SupportedCurrencyResponseModel.class
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public ExchangeRatesResponseModel getExchangeRates(String sourceCode, List<String> targetCodes) {
        try {
            String response = restTemplate.getForObject(
                buildUrlToGetExchangeRates(sourceCode, targetCodes),
                String.class
            );
            return objectMapper.readValue(response, ExchangeRatesResponseModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildUrlToGetExchangeRates(String sourceCode, List<String> targetCodes) {
        return UriComponentsBuilder.fromUriString(API_URL_TEMPLATE)
            .path(CURRENCY_RATES_PATH)
            .queryParam("access_key", ACCESS_KEY)
            .queryParam("base", sourceCode)
            .queryParam("symbols", String.join(",", targetCodes))
            .toUriString();
    }

}
