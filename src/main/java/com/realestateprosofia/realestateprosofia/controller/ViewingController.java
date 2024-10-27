package com.realestateprosofia.realestateprosofia.controller;

import com.realestateprosofia.realestateprosofia.controller.dto.ViewingDTO;
import com.realestateprosofia.realestateprosofia.controller.dto.ViewingTransferDTO;
import com.realestateprosofia.realestateprosofia.service.BuyerService;
import com.realestateprosofia.realestateprosofia.service.PropertyService;
import com.realestateprosofia.realestateprosofia.service.ViewingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/viewings")
public class ViewingController {

    private final ViewingService viewingService;
    private final BuyerService buyerService;
    private final PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<ViewingDTO>> getAllViewings() {
        final List<ViewingDTO> viewings = viewingService.getAllViewings();
        return ResponseEntity.ok(viewings);
    }

    @PostMapping
    public ResponseEntity<ViewingDTO> addViewing(@RequestBody final ViewingTransferDTO viewingTransferDTO) {
        final ViewingDTO viewing = viewingService.createViewing(
                propertyService.getPropertyById(viewingTransferDTO.getPropertyId()),
                buyerService.getBuyerById(viewingTransferDTO.getBuyerId())
        );
        return ResponseEntity.status(201).body(viewing);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViewingDTO> getViewingById(@PathVariable final Long id) {
        final ViewingDTO viewing = viewingService.getViewingDTOById(id);
        return ResponseEntity.ok(viewing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteViewing(@PathVariable final Long id) {
        viewingService.deleteViewing(id);
        return ResponseEntity.noContent().build();
    }
}