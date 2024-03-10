package com.example.currenciestesttask.currenciestesttask.repository;

import com.example.currenciestesttask.currenciestesttask.entity.CurrencyExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyExchangeRateRepository extends JpaRepository<CurrencyExchangeRate, Long> {
}
