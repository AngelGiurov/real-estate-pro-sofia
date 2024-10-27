package com.realestateprosofia.realestateprosofia.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;

    private BigDecimal budget = BigDecimal.ZERO;

    @OneToMany(mappedBy = "agency")
    @JsonManagedReference
    private List<Agent> agents;
}