package com.realestateprosofia.realestateprosofia.model;

import com.realestateprosofia.realestateprosofia.utils.BudgetRange;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@DiscriminatorValue("BUYER")
public class Buyer extends Client {

    @Embedded
    private BudgetRange budgetRange;

    @OneToMany(mappedBy = "buyer")
    private List<Viewing> viewings = new ArrayList<>();
}