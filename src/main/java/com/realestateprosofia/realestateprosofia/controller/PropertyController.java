package com.realestateprosofia.realestateprosofia.controller;

import com.realestateprosofia.realestateprosofia.controller.dto.PropertyDTO;
import com.realestateprosofia.realestateprosofia.service.PropertyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getAllProperties() {
        final List<PropertyDTO> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/search-price")
    public ResponseEntity<List<PropertyDTO>> searchPropertiesByPrice(@Valid @RequestBody final String maxPrice) {
        final BigDecimal bigDecimal = new BigDecimal(maxPrice);
        final List<PropertyDTO> properties = propertyService.searchPropertiesByPrice(bigDecimal);
        return ResponseEntity.ok(properties);
    }

    @PostMapping
    public ResponseEntity<PropertyDTO> addProperty(@RequestBody final PropertyDTO propertyDTO) {
        final PropertyDTO createdProperty = propertyService.addProperty(propertyDTO);
        return ResponseEntity.status(201).body(createdProperty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDTO> getPropertyById(@PathVariable final Long id) {
        final PropertyDTO property = propertyService.getPropertyDTOById(id);
        return ResponseEntity.ok(property);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyDTO> updateProperty(@PathVariable final Long id,
                                                      @RequestBody final PropertyDTO propertyDTO) {
        final PropertyDTO updatedProperty = propertyService.updateProperty(id, propertyDTO);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable final Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }
}