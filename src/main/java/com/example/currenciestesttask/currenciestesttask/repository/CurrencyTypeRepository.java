package com.example.currenciestesttask.currenciestesttask.repository;

import com.example.currenciestesttask.currenciestesttask.entity.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyTypeRepository extends JpaRepository<CurrencyType, Long> {

    CurrencyType findByCode(String code);

    List<CurrencyType> findByCodeIn(List<String> codes);
}
