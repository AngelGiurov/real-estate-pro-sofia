package com.realestateprosofia.realestateprosofia.repository;

import com.realestateprosofia.realestateprosofia.model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {

}