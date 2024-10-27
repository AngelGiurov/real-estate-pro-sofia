package com.realestateprosofia.realestateprosofia.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("SELLER")
public class Seller extends Client {

    @OneToOne(mappedBy = "seller", cascade = CascadeType.ALL)
    private Property property;
}