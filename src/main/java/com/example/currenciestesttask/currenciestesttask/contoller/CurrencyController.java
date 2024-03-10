package com.example.currenciestesttask.currenciestesttask.contoller;

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
@RequestMapping("/api/currencies")
public class CurrencyController {
    private final CurrencyServiceImpl currencyService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CurrencyController(CurrencyServiceImpl currencyService, ObjectMapper objectMapper) {
        this.currencyService = currencyService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<Currency> getAllCurrencies() {
        return currencyService.getCurrencies();
    }

//    @GetMapping("/{code}/exchange-rates")
//    public Double getExchangeRate(@PathVariable String code) {
//        return currencyService.getExchangeRate(code);
//    }

    @PostMapping(value = "/add")
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
