package com.realestateprosofia.realestateprosofia.controller;

import com.realestateprosofia.realestateprosofia.controller.dto.SellerDTO;
import com.realestateprosofia.realestateprosofia.service.SellerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sellers")
public class SellerController {

    private final SellerService sellerService;


    @GetMapping
    public ResponseEntity<List<SellerDTO>> getAllSellers() {
        final List<SellerDTO> sellers = sellerService.getAllSellers();
        return ResponseEntity.ok(sellers);
    }

    @PostMapping
    public ResponseEntity<SellerDTO> addSeller(@RequestBody final SellerDTO sellerDTO) {
        sellerService.addSeller(sellerDTO);
        return ResponseEntity.status(201).body(sellerDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerDTO> getSellerById(@PathVariable final Long id) {
        final SellerDTO seller = sellerService.getSellerDTOById(id);
        return ResponseEntity.ok(seller);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SellerDTO> updateSeller(@PathVariable final Long id,
                                                  @RequestBody final SellerDTO sellerDTO) {
        final SellerDTO updatedSeller = sellerService.updateSeller(id, sellerDTO);
        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable final Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}