package com.example.currenciestesttask.currenciestesttask.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class ExchangeRatesResponseModel {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("base")
    private String code;

    @JsonProperty("date")
    private String date;

    @JsonProperty("rates")
    private Map<String, Double> rates;
}
