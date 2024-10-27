package com.realestateprosofia.realestateprosofia.controller.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PurchaseDTO {
    private Long id;
    private LocalDateTime purchaseDate;
    private Long propertyId;
    private Long agentId;
    private Long buyerId;
    private Long sellerId;
    private BigDecimal price;
    private BigDecimal agencyCommission;
    private BigDecimal agentCommission;
}