package com.realestateprosofia.realestateprosofia.controller.dto;

import lombok.Data;

@Data
public class SellerDTO {
    private Long id;
    private String name;
    private String phone;
    private Long agentId;
    private Long propertyId;
}