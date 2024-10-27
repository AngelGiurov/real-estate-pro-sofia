package com.realestateprosofia.realestateprosofia.controller.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PropertyDTO {
    private Long id;
    private String description;
    private String address;
    private BigDecimal price;
    private Double area;
    private String type;
    private String apartmentType;
    private String constructionType;
    private String parcelType;
    private boolean regulated;
    private String houseType;
    private int parkingSpaces;
    private double yardArea;
    private Long sellerId;
    private Long agentId;
}