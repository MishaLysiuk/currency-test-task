package com.example.currenciestesttask.currenciestesttask.repository;

import com.example.currenciestesttask.currenciestesttask.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByCode(String code);
}
