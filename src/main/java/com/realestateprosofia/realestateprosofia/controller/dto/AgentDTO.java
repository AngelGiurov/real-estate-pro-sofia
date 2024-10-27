package com.realestateprosofia.realestateprosofia.controller.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class AgentDTO {
    private Long id;
    private String name;
    private String phone;
    private BigDecimal earnings;
    private Long agencyId;
    private List<Long> sellersId;
    private List<Long> buyersId;
    private List<Long> propertiesId;
    private List<Long> viewingsId;
}