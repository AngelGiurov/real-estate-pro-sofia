package com.realestateprosofia.realestateprosofia.controller.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AgencyDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private BigDecimal budget;
    private List<Long> agents;
}