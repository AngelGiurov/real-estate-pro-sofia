package com.realestateprosofia.realestateprosofia.controller;

import com.realestateprosofia.realestateprosofia.service.AgencyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agency")
public class AgencyController {

    private final AgencyService agencyService;

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getAgencyBalance() {
        final BigDecimal value = agencyService.getAgencyDTO().getBudget();
        return ResponseEntity.ok(value);
    }
}