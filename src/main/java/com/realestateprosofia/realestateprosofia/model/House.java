package com.realestateprosofia.realestateprosofia.model;

import com.realestateprosofia.realestateprosofia.model.enums.ConstructionType;
import com.realestateprosofia.realestateprosofia.model.enums.HouseType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("HOUSE")
public class House extends Property {

    @Enumerated(EnumType.STRING)
    private HouseType houseType;

    @Enumerated(EnumType.STRING)
    private ConstructionType constructionType;

    private int parkingSpaces;
    private double yardArea;
}