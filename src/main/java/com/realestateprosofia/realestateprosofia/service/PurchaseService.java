package com.realestateprosofia.realestateprosofia.service;

import com.realestateprosofia.realestateprosofia.controller.dto.PurchaseDTO;
import com.realestateprosofia.realestateprosofia.model.*;
import com.realestateprosofia.realestateprosofia.repository.PurchaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final AgencyService agencyService;

    public PurchaseDTO createPurchase(final Property property,
                                      final Buyer buyer) {
        boolean hasViewed = buyer.getViewings().stream()
                .anyMatch(viewing -> viewing.getProperty().getId().equals(property.getId()));

        if (!hasViewed) {
            throw new IllegalArgumentException("Buyer has not viewed this property.");
        }

        final BigDecimal commission = property.getPrice().multiply(new BigDecimal("0.03"));
        final BigDecimal agentCommission = commission.divide(new BigDecimal("2"));
        final BigDecimal agencyCommission = commission.subtract(agentCommission);

        final Agent agent = property.getAgent();
        agent.setEarnings(agent.getEarnings().add(agentCommission));

        agencyService.addToBudget(agencyCommission.multiply(new BigDecimal("2")));

        final Purchase purchase = new Purchase();
        purchase.setProperty(property);
        purchase.setBuyer(buyer);
        purchase.setSeller(property.getSeller());
        purchase.setAgent(agent);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setPrice(property.getPrice());
        purchase.setAgentCommission(agentCommission);
        purchase.setAgencyCommission(agencyCommission);

        final Purchase savedPurchase = purchaseRepository.save(purchase);
        return mapToDTO(savedPurchase);
    }

    public List<PurchaseDTO> getAllPurchases() {
        return purchaseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Purchase getPurchaseById(final Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase not found with ID: " + id));
    }

    public PurchaseDTO getPurchaseDTOById(final Long id) {
        final Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase not found with ID: " + id));
        return mapToDTO(purchase);
    }

    public void deletePurchase(Long id) {
        purchaseRepository.deleteById(id);
    }

    private PurchaseDTO mapToDTO(final Purchase purchase) {
        final PurchaseDTO dto = new PurchaseDTO();
        dto.setId(purchase.getId());
        dto.setPurchaseDate(purchase.getPurchaseDate());
        dto.setPropertyId(purchase.getProperty().getId());
        dto.setAgentId(purchase.getAgent().getId());
        dto.setBuyerId(purchase.getBuyer().getId());
        dto.setSellerId(purchase.getSeller().getId());
        dto.setPrice(purchase.getPrice());
        dto.setAgencyCommission(purchase.getAgencyCommission());
        dto.setAgentCommission(purchase.getAgentCommission());
        return dto;
    }

    private Purchase mapFromDTO(final PurchaseDTO dto,
                                final Property property,
                                final Buyer buyer,
                                final Agent agent,
                                final Seller seller) {
        final Purchase purchase = new Purchase();
        purchase.setId(dto.getId());
        purchase.setPurchaseDate(dto.getPurchaseDate());
        purchase.setProperty(property);
        purchase.setAgent(agent);
        purchase.setBuyer(buyer);
        purchase.setSeller(seller);
        purchase.setPrice(dto.getPrice());
        purchase.setAgencyCommission(dto.getAgencyCommission());
        purchase.setAgentCommission(dto.getAgentCommission());
        return purchase;
    }
}