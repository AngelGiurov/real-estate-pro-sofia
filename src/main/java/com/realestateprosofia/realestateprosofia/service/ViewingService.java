package com.realestateprosofia.realestateprosofia.service;

import com.realestateprosofia.realestateprosofia.controller.dto.ViewingDTO;
import com.realestateprosofia.realestateprosofia.model.Buyer;
import com.realestateprosofia.realestateprosofia.model.Property;
import com.realestateprosofia.realestateprosofia.model.Viewing;
import com.realestateprosofia.realestateprosofia.repository.ViewingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ViewingService {

    private final ViewingRepository viewingRepository;

    public ViewingDTO createViewing(final Property property,
                                    final Buyer buyer) {
        if (buyer.getBudgetRange() == null) {
            throw new IllegalArgumentException("Buyer has no budget range defined.");
        }

        if (property.getPrice().compareTo(buyer.getBudgetRange().getMax()) > 0) {
            throw new IllegalArgumentException("Property price exceeds buyer's max budget.");
        }

        final Viewing viewing = new Viewing();
        viewing.setProperty(property);
        viewing.setBuyer(buyer);
        viewing.setAgent(property.getAgent());
        viewing.setViewingDate(LocalDateTime.now());

        Viewing savedViewing = viewingRepository.save(viewing);
        return mapToDTO(savedViewing);
    }

    public List<ViewingDTO> getAllViewings() {
        return viewingRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ViewingDTO getViewingDTOById(final Long id) {
        final Viewing viewing = viewingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viewing not found with ID: " + id));
        return mapToDTO(viewing);
    }

    public Viewing getViewingById(final Long id) {
        return viewingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viewing not found with ID: " + id));
    }

    public void deleteViewing(final Long id) {
        viewingRepository.deleteById(id);
    }

    private ViewingDTO mapToDTO(final Viewing viewing) {
        ViewingDTO dto = new ViewingDTO();
        dto.setId(viewing.getId());
        dto.setPropertyId(viewing.getProperty().getId());
        dto.setBuyerId(viewing.getBuyer().getId());
        dto.setAgentId(viewing.getAgent().getId());
        dto.setDate(viewing.getViewingDate());
        return dto;
    }

    private Viewing mapFromDTO(final ViewingDTO dto,
                               final Property property,
                               final Buyer buyer) {
        final Viewing viewing = new Viewing();
        viewing.setProperty(property);
        viewing.setBuyer(buyer);
        viewing.setAgent(property.getAgent());
        viewing.setViewingDate(dto.getDate());
        return viewing;
    }
}