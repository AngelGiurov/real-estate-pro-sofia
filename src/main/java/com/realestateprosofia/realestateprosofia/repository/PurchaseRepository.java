package com.realestateprosofia.realestateprosofia.repository;
import com.realestateprosofia.realestateprosofia.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}