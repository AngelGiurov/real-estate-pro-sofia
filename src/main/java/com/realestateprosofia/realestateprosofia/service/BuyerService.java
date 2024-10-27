package com.realestateprosofia.realestateprosofia.service;

import com.realestateprosofia.realestateprosofia.controller.dto.BuyerDTO;
import com.realestateprosofia.realestateprosofia.model.Agent;
import com.realestateprosofia.realestateprosofia.model.Buyer;
import com.realestateprosofia.realestateprosofia.utils.BudgetRange;
import com.realestateprosofia.realestateprosofia.model.Viewing;
import com.realestateprosofia.realestateprosofia.repository.BuyerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BuyerService {

    private final BuyerRepository buyerRepository;
    private final AgentService agentService;


    public void addBuyer(final BuyerDTO buyerDTO) {
        if (buyerDTO.getAgentId() == null) {
            final Agent randomAgent = agentService.getRandomAgent();
            buyerDTO.setAgentId(randomAgent.getId());
        }

        final Buyer buyer = mapFromDTO(buyerDTO);

        buyerRepository.save(buyer);

        final Agent agent = buyer.getAgent();
        if (agent != null) {
            agent.getBuyers().add(buyer);
        }
    }


    public List<BuyerDTO> getAllBuyers() {
        return buyerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    public Buyer getBuyerById(final Long id) {
        return buyerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Buyer not found with ID: " + id));
    }


    public BuyerDTO getBuyerDTOById(final Long id){
        final Buyer buyer = getBuyerById(id);
        return mapToDTO(buyer);
    }

    public BuyerDTO updateBuyer(final Long id,
                                final BuyerDTO buyerDTO) {
        final Buyer buyer = buyerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Buyer not found with ID: " + id));
        mapFromDTO(buyerDTO, buyer);
        final Buyer updatedBuyer = buyerRepository.save(buyer);
        return mapToDTO(updatedBuyer);
    }

    public void deleteBuyer(final Long id) {
        buyerRepository.deleteById(id);
    }


    public BuyerDTO mapToDTO(final Buyer buyer) {
        final BuyerDTO buyerDTO = new BuyerDTO();
        buyerDTO.setId(buyer.getId());
        buyerDTO.setName(buyer.getName());
        buyerDTO.setPhone(buyer.getPhone());

        if (buyer.getBudgetRange() != null) {
            buyerDTO.setMinBudget(buyer.getBudgetRange().getMin());
            buyerDTO.setMaxBudget(buyer.getBudgetRange().getMax());
        }

        buyerDTO.setAgentId(buyer.getAgent() != null ? buyer.getAgent().getId() : null);

        final List<Long> viewingsId = buyer.getViewings() == null
                ? List.of()
                : buyer.getViewings().stream()
                .map(Viewing::getId)
                .collect(Collectors.toList());
        buyerDTO.setViewings(viewingsId);

        return buyerDTO;
    }

    public Buyer mapFromDTO(final BuyerDTO buyerDTO) {
        final Buyer buyer = new Buyer();
        mapFromDTO(buyerDTO, buyer);
        return buyer;
    }

    private void mapFromDTO(final BuyerDTO buyerDTO,
                            final Buyer buyer) {
        buyer.setName(buyerDTO.getName());
        buyer.setPhone(buyerDTO.getPhone());

        if (buyerDTO.getMinBudget() != null && buyerDTO.getMaxBudget() != null) {
            if (buyerDTO.getMinBudget().compareTo(buyerDTO.getMaxBudget()) > 0) {
                throw new IllegalArgumentException("minBudget cannot be greater than maxBudget");
            }
            final BudgetRange budgetRange = new BudgetRange(buyerDTO.getMinBudget(), buyerDTO.getMaxBudget());
            buyer.setBudgetRange(budgetRange);
        } else {
            throw new IllegalArgumentException("Both minBudget and maxBudget must be provided");
        }

        if (buyerDTO.getAgentId() != null) {
            final Agent agent = agentService.getAgentById(buyerDTO.getAgentId());
            buyer.setAgent(agent);
            agent.getBuyers().add(buyer);
        }
    }
}
