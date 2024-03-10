package com.example.currenciestesttask.currenciestesttask.task;

import com.example.currenciestesttask.currenciestesttask.entity.CurrencyType;
import com.example.currenciestesttask.currenciestesttask.response.SupportedCurrencyResponseModel;
import com.example.currenciestesttask.currenciestesttask.service.CurrencyTypeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class InitCurrencyTypes {

    private final CurrencyTypeService currencyTypeService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final String CURRENCY_API_SUPPORTED_LIST = "https://api.exchangeratesapi.io/v1/symbols?access_key=5da87818ddc7d15139dfedfd98893ee8";

    @Autowired
    public InitCurrencyTypes(
        CurrencyTypeService currencyTypeService,
        RestTemplate restTemplate,
        ObjectMapper objectMapper) {
        this.currencyTypeService = currencyTypeService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void initCurrencyType() throws JsonProcessingException {
        List<CurrencyType> existingCurrencyTypes = currencyTypeService.getCurrencyTypes();
        if (existingCurrencyTypes.isEmpty()) {
            String response = restTemplate.getForObject(CURRENCY_API_SUPPORTED_LIST, String.class);
            SupportedCurrencyResponseModel currencyResponse = objectMapper.readValue(
                response,
                SupportedCurrencyResponseModel.class
            );
            List<CurrencyType> currencyTypes = currencyResponse.getSymbols().keySet().stream()
                .map(el -> {
                    CurrencyType newCurrencyType = new CurrencyType();
                    newCurrencyType.setCode(el);
                    return newCurrencyType;
                }).toList();
            currencyTypeService.saveAll(currencyTypes);
        }
    }
}
