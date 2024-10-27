package com.realestateprosofia.realestateprosofia.repository;

import com.realestateprosofia.realestateprosofia.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}