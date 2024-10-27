package com.realestateprosofia.realestateprosofia.utils;

import lombok.AllArgsConstructor;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BudgetRange {
    @Column(name = "budget_min")
    private BigDecimal min;

    @Column(name = "budget_max")
    private BigDecimal max;
}