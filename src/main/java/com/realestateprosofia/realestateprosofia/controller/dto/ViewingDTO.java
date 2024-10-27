package com.realestateprosofia.realestateprosofia.controller.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ViewingDTO {
    private Long id;
    private Long propertyId;
    private Long buyerId;
    private Long agentId;
    private LocalDateTime date;
}