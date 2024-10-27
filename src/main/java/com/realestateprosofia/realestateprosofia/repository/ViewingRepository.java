package com.realestateprosofia.realestateprosofia.repository;

import com.realestateprosofia.realestateprosofia.model.Viewing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewingRepository extends JpaRepository<Viewing, Long> {

}