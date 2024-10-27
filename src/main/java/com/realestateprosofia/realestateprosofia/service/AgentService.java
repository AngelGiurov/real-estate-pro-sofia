package com.realestateprosofia.realestateprosofia.service;

import com.realestateprosofia.realestateprosofia.controller.dto.AgentDTO;
import com.realestateprosofia.realestateprosofia.model.*;
import com.realestateprosofia.realestateprosofia.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AgentService {

    private final AgentRepository agentRepository;
    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;
    private final AgencyService agencyService;
    private final PropertyRepository propertyRepository;
    private final ViewingRepository viewingRepository;
    private final Random random = new Random();

    public void addAgent(final AgentDTO agentDTO) {
        final Agent agent = mapFromDTO(agentDTO);
        agentRepository.save(agent);
        updateRelatedEntities(agent,agentDTO);
    }

    private void updateRelatedEntities(final Agent updatedAgent,
                                       final AgentDTO agentDTO) {

        final List<Buyer> buyers = agentDTO.getBuyersId().stream()
                .map(buyerId -> buyerRepository.findById(buyerId)
                        .orElseThrow(() -> new RuntimeException("Buyer not found with ID: " + buyerId)))
                .peek(buyer -> buyer.setAgent(updatedAgent))
                .collect(Collectors.toList());
        updatedAgent.setBuyers(buyers);

        final List<Seller> sellers = agentDTO.getSellersId().stream()
                .map(sellerId -> sellerRepository.findById(sellerId)
                        .orElseThrow(() -> new RuntimeException("Seller not found with ID: " + sellerId)))
                .peek(seller -> seller.setAgent(updatedAgent))
                .collect(Collectors.toList());
        updatedAgent.setSellers(sellers);

        final List<Property> properties = agentDTO.getPropertiesId().stream()
                .map(propertyId -> propertyRepository.findById(propertyId)
                        .orElseThrow(() -> new RuntimeException("Property not found with ID: " + propertyId)))
                .peek(property -> property.setAgent(updatedAgent))
                .collect(Collectors.toList());
        updatedAgent.setProperties(properties);

        final List<Viewing> viewings = agentDTO.getViewingsId().stream()
                .map(viewingId -> viewingRepository.findById(viewingId)
                        .orElseThrow(() -> new RuntimeException("Viewing not found with ID: " + viewingId)))
                .peek(viewing -> viewing.setAgent(updatedAgent))
                .collect(Collectors.toList());
        updatedAgent.setViewings(viewings);
    }

    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    public void deleteAgent(final Long id) {
        final Agent agent = getAgentById(id);

        final List<Viewing> viewingsToDelete = agent.getViewings();
        viewingRepository.deleteAll(viewingsToDelete);

        agent.getBuyers().forEach(buyer -> buyer.setAgent(null));
        buyerRepository.saveAll(agent.getBuyers());

        agent.getSellers().forEach(seller -> seller.setAgent(null));
        sellerRepository.saveAll(agent.getSellers());

        agent.getProperties().forEach(property -> property.setAgent(null));
        propertyRepository.saveAll(agent.getProperties());

        agentRepository.deleteById(id);
    }

    public Agent getRandomAgent() {
        final List<Agent> agents = agentRepository.findAll();
        return agents.get(random.nextInt(agents.size()));
    }

    public AgentDTO getAgentDTOById(final Long id) {
        final Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agent not found with ID: " + id));
        return mapToDTO(agent);
    }

    public Agent getAgentById(final Long id) {
        return agentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agent not found with ID: " + id));
    }

    public AgentDTO updateAgent(final Long id,
                                final AgentDTO agentDTO) {
        final Agent existingAgent = getAgentById(id);
        existingAgent.setName(agentDTO.getName());
        existingAgent.setPhone(agentDTO.getPhone());
        existingAgent.setEarnings(agentDTO.getEarnings());
        existingAgent.setAgency(agencyService.getAgency());

        final List<Buyer> buyers = agentDTO.getBuyersId().stream()
                .map(buyerId -> buyerRepository.findById(buyerId)
                        .orElseThrow(() -> new RuntimeException("Buyer not found with ID: " + buyerId)))
                .toList();
        existingAgent.getBuyers().clear();
        existingAgent.getBuyers().addAll(buyers);

        final List<Seller> sellers = agentDTO.getSellersId().stream()
                .map(sellerId -> sellerRepository.findById(sellerId)
                        .orElseThrow(() -> new RuntimeException("Seller not found with ID: " + sellerId)))
                .toList();
        existingAgent.getSellers().clear();
        existingAgent.getSellers().addAll(sellers);

        final List<Property> properties = agentDTO.getPropertiesId().stream()
                .map(propertyId -> propertyRepository.findById(propertyId)
                        .orElseThrow(() -> new RuntimeException("Property not found with ID: " + propertyId)))
                .toList();
        existingAgent.getProperties().clear();
        existingAgent.getProperties().addAll(properties);

        final List<Viewing> viewings = agentDTO.getViewingsId().stream()
                .map(viewingId -> viewingRepository.findById(viewingId)
                        .orElseThrow(() -> new RuntimeException("Viewing not found with ID: " + viewingId)))
                .toList();
        existingAgent.getViewings().clear();
        existingAgent.getViewings().addAll(viewings);

        final Agent updatedAgent = agentRepository.save(existingAgent);

        return mapToDTO(updatedAgent);
    }


    public List<AgentDTO> getAgentsSortedByEarnings() {
        final List<Agent> agentList = agentRepository.findAll()
                .stream()
                .sorted((a1, a2) -> a2.getEarnings().compareTo(a1.getEarnings()))
                .toList();
        return agentList.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AgentDTO mapToDTO(final Agent agent) {
        final AgentDTO agentDTO = new AgentDTO();
        agentDTO.setId(agent.getId());
        agentDTO.setName(agent.getName());
        agentDTO.setPhone(agent.getPhone());
        agentDTO.setEarnings(agent.getEarnings());

        final List<Long> buyerIds = (agent.getBuyers() == null || agent.getBuyers().isEmpty())
                ? List.of()
                : agent.getBuyers().stream()
                .map(Buyer::getId)
                .toList();

        final List<Long> sellerIds = (agent.getSellers() == null || agent.getSellers().isEmpty())
                ? List.of()
                : agent.getSellers().stream()
                .map(Seller::getId)
                .toList();

        final List<Long> propertiesIds = (agent.getProperties() == null || agent.getProperties().isEmpty())
                ? List.of()
                : agent.getProperties().stream()
                .map(Property::getId)
                .toList();

        final List<Long> viewingsId = (agent.getViewings() == null || agent.getViewings().isEmpty())
                ? List.of()
                : agent.getViewings().stream()
                .map(Viewing::getId)
                .toList();

        agentDTO.setBuyersId(buyerIds);
        agentDTO.setAgencyId(agent.getAgency() != null ? agent.getAgency().getId() : null);
        agentDTO.setSellersId(sellerIds);
        agentDTO.setPropertiesId(propertiesIds);
        agentDTO.setViewingsId(viewingsId);

        return agentDTO;
    }

    public Agent mapFromDTO(final AgentDTO agentDTO) {
        final Agent agent = new Agent();

        agent.setId(agentDTO.getId());
        agent.setName(agentDTO.getName());
        agent.setPhone(agentDTO.getPhone());
        agent.setEarnings(agentDTO.getEarnings());
        agent.setAgency(agencyService.getAgency());


        if (!agentDTO.getBuyersId().isEmpty()) {
            final List<Buyer> buyers = agentDTO.getBuyersId().stream()
                    .map(buyerId -> buyerRepository.findById(buyerId)
                            .orElseThrow(() -> new RuntimeException("Buyer not found with ID: " + buyerId)))
                    .peek(buyer -> {
                        buyer.setAgent(agent);
                    })
                    .collect(Collectors.toList());
            agent.setBuyers(buyers);
        }

        if (!agentDTO.getSellersId().isEmpty()) {
            final List<Seller> sellers = agentDTO.getSellersId().stream()
                    .map(sellerId -> sellerRepository.findById(sellerId)
                            .orElseThrow(() -> new RuntimeException("Seller not found with ID: " + sellerId)))
                    .peek(seller -> {
                        seller.setAgent(agent);
                    })
                    .collect(Collectors.toList());
            agent.setSellers(sellers);
        }

        if (!agentDTO.getPropertiesId().isEmpty()) {
            final List<Property> properties = agentDTO.getPropertiesId().stream()
                    .map(propertyId -> propertyRepository.findById(propertyId)
                            .orElseThrow(() -> new RuntimeException("Property not found with ID: " + propertyId)))
                    .peek(property -> {
                        property.setAgent(agent);
                    })
                    .collect(Collectors.toList());
            agent.setProperties(properties);
        }

        if (!agentDTO.getViewingsId().isEmpty()) {
            final List<Viewing> viewings = agentDTO.getViewingsId().stream()
                    .map(viewingsId -> viewingRepository.findById(viewingsId)
                            .orElseThrow(() -> new RuntimeException("Property not found with ID: " + viewingsId)))
                    .peek(viewing -> {
                        viewing.setAgent(agent);
                    })
                    .collect(Collectors.toList());
            agent.setViewings(viewings);
        }
        return agent;
    }

    public List<AgentDTO> getAllAgentsFromDTO() {
        return getAllAgents().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}