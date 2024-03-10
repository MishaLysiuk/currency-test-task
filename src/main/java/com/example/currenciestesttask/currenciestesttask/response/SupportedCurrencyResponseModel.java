package com.example.currenciestesttask.currenciestesttask.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class SupportedCurrencyResponseModel {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("symbols")
    private Map<String, String> symbols;
}
