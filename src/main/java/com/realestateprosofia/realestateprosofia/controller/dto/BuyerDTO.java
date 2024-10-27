package com.realestateprosofia.realestateprosofia.controller.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BuyerDTO {
    private Long id;
    private String name;
    private String phone;
    private BigDecimal minBudget;
    private BigDecimal maxBudget;
    private Long agentId;
    private List<Long> viewings;
}