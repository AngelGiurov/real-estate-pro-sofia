package com.realestateprosofia.realestateprosofia.model;

import com.realestateprosofia.realestateprosofia.model.enums.ParcelType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("PARCEL")
public class Parcel extends Property {

    @Enumerated(EnumType.STRING)
    private ParcelType parcelType;

    private boolean regulated;
}