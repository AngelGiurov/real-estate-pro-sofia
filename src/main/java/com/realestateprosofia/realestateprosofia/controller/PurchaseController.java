package com.realestateprosofia.realestateprosofia.controller;

import com.realestateprosofia.realestateprosofia.controller.dto.PurchaseDTO;
import com.realestateprosofia.realestateprosofia.controller.dto.PurchaseTransferDTO;
import com.realestateprosofia.realestateprosofia.service.BuyerService;
import com.realestateprosofia.realestateprosofia.service.PropertyService;
import com.realestateprosofia.realestateprosofia.service.PurchaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final BuyerService buyerService;
    private final PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<PurchaseDTO>> getAllPurchases() {
        final List<PurchaseDTO> purchases = purchaseService.getAllPurchases();
        return ResponseEntity.ok(purchases);
    }

    @PostMapping
    public ResponseEntity<PurchaseDTO> createPurchase(@RequestBody final PurchaseTransferDTO purchaseTransferDTO) {
        final PurchaseDTO purchase = purchaseService.createPurchase(
                propertyService.getPropertyById(purchaseTransferDTO.getPropertyId()),
                buyerService.getBuyerById(purchaseTransferDTO.getBuyerId()));
        return ResponseEntity.status(201).body(purchase);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDTO> getPurchaseById(@PathVariable final Long id) {
        final PurchaseDTO purchase = purchaseService.getPurchaseDTOById(id);
        return ResponseEntity.ok(purchase);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable final Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.noContent().build();
    }
}