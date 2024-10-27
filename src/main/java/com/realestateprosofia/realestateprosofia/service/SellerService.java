package com.realestateprosofia.realestateprosofia.service;

import com.realestateprosofia.realestateprosofia.controller.dto.SellerDTO;
import com.realestateprosofia.realestateprosofia.model.Agent;
import com.realestateprosofia.realestateprosofia.model.Seller;
import com.realestateprosofia.realestateprosofia.repository.PropertyRepository;
import com.realestateprosofia.realestateprosofia.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final AgentService agentService;
    private final PropertyRepository propertyRepository;

    public void addSeller(final SellerDTO sellerDTO) {
        if (sellerDTO.getAgentId() != null){
            final Agent agent = agentService.getRandomAgent();
            sellerDTO.setAgentId(agent.getId());
        }

        final Seller seller = mapFromDTO(sellerDTO);

        sellerRepository.save(seller);

        final Agent agent = seller.getAgent();
        if (agent != null) {
            agent.getSellers().add(seller);
        }
    }

    public List<SellerDTO> getAllSellers() {
        return sellerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Seller getSellerById(final Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seller not found with ID: " + id));
    }

    public SellerDTO getSellerDTOById(final Long id) {
        final Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seller not found with ID: " + id));
        return mapToDTO(seller);
    }

    public SellerDTO updateSeller(final Long id,
                                  final SellerDTO sellerDTO) {
        final Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seller not found with ID: " + id));

        seller.setName(sellerDTO.getName());
        seller.setPhone(sellerDTO.getPhone());

        final Agent agent = agentService.getAgentById(sellerDTO.getAgentId());
        seller.setAgent(agent);

        final Seller updatedSeller = sellerRepository.save(seller);
        return mapToDTO(updatedSeller);
    }

    public void deleteSeller(final Long id) {
        sellerRepository.deleteById(id);
    }

    private SellerDTO mapToDTO(final Seller seller) {
        final SellerDTO dto = new SellerDTO();
        dto.setId(seller.getId());
        dto.setName(seller.getName());
        dto.setPhone(seller.getPhone());
        dto.setAgentId(seller.getAgent() != null ? seller.getAgent().getId() : null);
        dto.setPropertyId(seller.getProperty() != null ? seller.getProperty().getId() : null);
        return dto;
    }

    private Seller mapFromDTO(final SellerDTO dto) {
        final Seller seller = new Seller();
        seller.setName(dto.getName());
        seller.setPhone(dto.getPhone());
        seller.setId(dto.getId());
        seller.setProperty(propertyRepository.findById(dto.getId()).get());
        if (dto.getAgentId() != null) {
            final Agent agent = agentService.getAgentById(dto.getAgentId());
            seller.setAgent(agent);
        }
        return seller;
    }
}