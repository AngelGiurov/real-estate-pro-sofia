package com.realestateprosofia.realestateprosofia.repository;

import com.realestateprosofia.realestateprosofia.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

}
