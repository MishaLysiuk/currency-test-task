package com.example.currenciestesttask.currenciestesttask.task;

import com.example.currenciestesttask.currenciestesttask.service.CurrencyExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateUpdater {

    private final CurrencyExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateUpdater(CurrencyExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Scheduled(fixedRateString ="${scheduled.task.updateRates.timeInMs}")
    public void updateRates() {
        exchangeRateService.updateRates();
    }
}
