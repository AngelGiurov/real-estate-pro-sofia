package com.realestateprosofia.realestateprosofia.service;

import com.realestateprosofia.realestateprosofia.controller.dto.AgencyDTO;
import com.realestateprosofia.realestateprosofia.model.Agency;
import com.realestateprosofia.realestateprosofia.model.Agent;
import com.realestateprosofia.realestateprosofia.repository.AgencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class AgencyService {

    private final AgencyRepository agencyRepository;

    public AgencyDTO getAgencyDTO() {
        final Agency agency = agencyRepository.findAll().getFirst();
        return mapToDTO(agency);
    }

    public Agency getAgency() {
        return agencyRepository.findAll().getFirst();
    }

    public void addToBudget(final BigDecimal amount) {
        final Agency agency = getAgency();
        agency.setBudget(agency.getBudget().add(amount));
        agencyRepository.save(agency);
    }
    public AgencyDTO mapToDTO(final Agency agency) {
        final AgencyDTO agencyDTO = new AgencyDTO();
        agencyDTO.setName(agency.getName());
        agencyDTO.setId(agency.getId());
        agencyDTO.setPhone(agency.getPhone());
        agencyDTO.setBudget(agency.getBudget());
        agencyDTO.setAddress(agency.getAddress());
        final List<Long> agentsIds = (agency.getAgents() == null || agency.getAgents().isEmpty())
                ? List.of()  // Default to empty list if buyers is null or empty
                : agency.getAgents().stream()
                .map(Agent::getId)
                .toList();
        agencyDTO.setAgents(agentsIds);
        return agencyDTO;
    }
}