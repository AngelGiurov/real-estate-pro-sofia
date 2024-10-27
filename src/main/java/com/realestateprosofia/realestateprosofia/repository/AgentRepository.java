package com.realestateprosofia.realestateprosofia.repository;

import com.realestateprosofia.realestateprosofia.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

}