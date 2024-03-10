package com.example.currenciestesttask.currenciestesttask.service;

import com.example.currenciestesttask.currenciestesttask.entity.CurrencyType;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CurrencyTypeService {

    List<CurrencyType> getCurrencyTypes();

    CurrencyType getCurrencyTypeByCode(String code);

    List<CurrencyType> findByCodeIn(List<String> codes);

    void saveAll(List<CurrencyType> currencyTypes);
}
