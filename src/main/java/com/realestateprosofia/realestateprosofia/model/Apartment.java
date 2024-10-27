package com.realestateprosofia.realestateprosofia.model;

import com.realestateprosofia.realestateprosofia.model.enums.ApartmentType;
import com.realestateprosofia.realestateprosofia.model.enums.ConstructionType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("APARTMENT")
public class Apartment extends Property {

    @Enumerated(EnumType.STRING)
    private ApartmentType apartmentType;

    @Enumerated(EnumType.STRING)
    private ConstructionType constructionType;
}