package com.realestateprosofia.realestateprosofia.controller;

import com.realestateprosofia.realestateprosofia.controller.dto.BuyerDTO;
import com.realestateprosofia.realestateprosofia.service.BuyerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/buyers")
public class BuyerController {

    private final BuyerService buyerService;

    @GetMapping
    public ResponseEntity<List<BuyerDTO>> getAllBuyers() {
        final List<BuyerDTO> buyers = buyerService.getAllBuyers();
        return ResponseEntity.ok(buyers);
    }

    @PostMapping
    public ResponseEntity<BuyerDTO> addBuyer(@RequestBody final BuyerDTO buyerDTO) {
        buyerService.addBuyer(buyerDTO);
        return ResponseEntity.status(201).body(buyerDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuyerDTO> getBuyerById(@PathVariable final Long id) {
        final BuyerDTO buyer = buyerService.getBuyerDTOById(id);
        return ResponseEntity.ok(buyer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuyerDTO> updateBuyer(@PathVariable final Long id,
                                                @RequestBody final BuyerDTO buyer) {
        final BuyerDTO updatedBuyer = buyerService.updateBuyer(id,buyer);
        return ResponseEntity.ok(updatedBuyer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuyer(@PathVariable final Long id) {
        buyerService.deleteBuyer(id);
        return ResponseEntity.noContent().build();
    }
}