package com.example.currenciestesttask.currenciestesttask.contoller;

import com.example.currenciestesttask.currenciestesttask.dto.CurrencyDto;
import com.example.currenciestesttask.currenciestesttask.dto.CurrencyExchangeRateDto;
import com.example.currenciestesttask.currenciestesttask.service.CurrencyExchangeRateService;
import com.example.currenciestesttask.currenciestesttask.service.CurrencyService;
import com.example.currenciestesttask.currenciestesttask.service.impl.CurrencyServiceImpl;
import com.example.currenciestesttask.currenciestesttask.entity.Currency;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final CurrencyExchangeRateService exchangeRateService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CurrencyController(
        CurrencyService currencyService,
        ObjectMapper objectMapper,
        CurrencyExchangeRateService exchangeRateService
    ) {
        this.currencyService = currencyService;
        this.objectMapper = objectMapper;
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public List<CurrencyDto> getAllCurrencies() {
        return currencyService.getCurrencies();
    }

    @GetMapping("/{code}/exchange-rates")
    public CurrencyExchangeRateDto getExchangeRate(@PathVariable String code) {
        return exchangeRateService.getCurrencyWithRates(code.toUpperCase());
    }

    @PostMapping
    public ResponseEntity<String> addCurrency(@RequestBody() String json) {
        try {
            String code = objectMapper.readTree(json).get("currencyCode").asText().toUpperCase();
            if (!currencyService.currencyAlreadyAdded(code)) {
                currencyService.addCurrency(code);
                return ResponseEntity.ok("Currency successfully: " + code);
            }
            return ResponseEntity.ok("Currency already added: " + code);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
