package com.realestateprosofia.realestateprosofia.service;

import com.realestateprosofia.realestateprosofia.controller.dto.PropertyDTO;
import com.realestateprosofia.realestateprosofia.model.*;
import com.realestateprosofia.realestateprosofia.model.enums.ApartmentType;
import com.realestateprosofia.realestateprosofia.model.enums.ConstructionType;
import com.realestateprosofia.realestateprosofia.model.enums.HouseType;
import com.realestateprosofia.realestateprosofia.model.enums.ParcelType;
import com.realestateprosofia.realestateprosofia.repository.PropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final SellerService sellerService;
    private final AgentService agentService;


    public PropertyDTO addProperty(final PropertyDTO propertyDTO) {
        final Property property = mapFromDTO(propertyDTO);
        if (propertyDTO.getAgentId() != null){
            property.setAgent(agentService.getRandomAgent());
        }
        final Property savedProperty = propertyRepository.save(property);
        return mapToDTO(savedProperty);
    }

    public List<PropertyDTO> getAllProperties() {
        return propertyRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PropertyDTO> searchPropertiesByPrice(final BigDecimal maxPrice) {
        return propertyRepository.findByPriceGreaterThanEqualOrderByPriceDesc(maxPrice).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PropertyDTO getPropertyDTOById(final Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found with ID: " + id));
        return mapToDTO(property);
    }

    public Property getPropertyById(final Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found with ID: " + id));
    }

    public PropertyDTO updateProperty(final Long id,
                                      final PropertyDTO propertyDTO) {
        final Property property = mapFromDTO(propertyDTO);
        property.setId(id);
        final Property updatedProperty = propertyRepository.save(property);
        return mapToDTO(updatedProperty);
    }

    public void deleteProperty(final Long id) {
        propertyRepository.deleteById(id);
    }

    private PropertyDTO mapToDTO(final Property property) {
        final PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setId(property.getId());
        propertyDTO.setType(property.getType());
        propertyDTO.setPrice(property.getPrice());
        propertyDTO.setAgentId(property.getAgent() != null ? property.getAgent().getId() : null);
        propertyDTO.setAddress(property.getAddress());
        propertyDTO.setArea(property.getArea());
        propertyDTO.setDescription(property.getDescription());
        propertyDTO.setSellerId(property.getSeller().getId());
        switch (property) {
            case Apartment apartment -> {
                propertyDTO.setApartmentType(apartment.getApartmentType().name());
                propertyDTO.setConstructionType(apartment.getConstructionType().name());
            }
            case Parcel parcel -> {
                propertyDTO.setParcelType(parcel.getParcelType().name());
                propertyDTO.setRegulated(parcel.isRegulated());
            }
            case House house -> {
                propertyDTO.setHouseType(house.getHouseType().name());
                propertyDTO.setConstructionType(house.getConstructionType().name());
                propertyDTO.setParkingSpaces(house.getParkingSpaces());
                propertyDTO.setYardArea(house.getYardArea());
            }
            default -> {
            }
        }

        return propertyDTO;
    }

    private Property mapFromDTO(final PropertyDTO propertyDTO) {
        final Property property;

        switch (propertyDTO.getType()) {
            case "APARTMENT" -> {
                final Apartment apartment = new Apartment();
                apartment.setApartmentType(ApartmentType.valueOf(propertyDTO.getApartmentType()));
                apartment.setConstructionType(ConstructionType.valueOf(propertyDTO.getConstructionType()));
                property = apartment;
            }
            case "PARCEL" -> {
                final Parcel parcel = new Parcel();
                parcel.setParcelType(ParcelType.valueOf(propertyDTO.getParcelType()));
                parcel.setRegulated(propertyDTO.isRegulated());
                property = parcel;
            }
            case "HOUSE" -> {
                final House house = new House();
                house.setHouseType(HouseType.valueOf(propertyDTO.getHouseType()));
                house.setConstructionType(ConstructionType.valueOf(propertyDTO.getConstructionType()));
                house.setParkingSpaces(propertyDTO.getParkingSpaces());
                house.setYardArea(propertyDTO.getYardArea());
                property = house;
            }
            default -> throw new IllegalArgumentException("Unknown property type: " + propertyDTO.getType());
        }
        property.setId(propertyDTO.getId());
        property.setType(propertyDTO.getType());
        property.setPrice(propertyDTO.getPrice());
        property.setAddress(propertyDTO.getAddress());
        property.setArea(propertyDTO.getArea());
        property.setDescription(propertyDTO.getDescription());
        property.setAgent(agentService.getAgentById(propertyDTO.getAgentId()));
        property.setSeller(sellerService.getSellerById(propertyDTO.getSellerId()));
        return property;
    }
}