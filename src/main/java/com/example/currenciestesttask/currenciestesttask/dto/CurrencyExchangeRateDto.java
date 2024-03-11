package com.example.currenciestesttask.currenciestesttask.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CurrencyExchangeRateDto {

    private String sourceCode;

    private Map<String, Double> rates;
}
