package com.salon.controller;

import com.salon.model.ServiceOffering;
import com.salon.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-offering")
public class ServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<Set<ServiceOffering>> getServicesBySalonId(
            @PathVariable Long salonId,
            @RequestParam(required = false) Long categoryId
    ) {
        Set<ServiceOffering> serviceOfferings = serviceOfferingService.getAllServicesBySalonId(salonId, categoryId);

        return ResponseEntity.ok(serviceOfferings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOffering> getServicesById(
            @PathVariable Long id
    ) throws Exception {
        ServiceOffering serviceOffering = serviceOfferingService.getServiceById(id);

        return ResponseEntity.ok(serviceOffering);
    }

    @GetMapping("/list/{ids}")
    public ResponseEntity<Set<ServiceOffering>> getServicesByIds(
            @PathVariable Set<Long> ids
    ) {
        Set<ServiceOffering> serviceOfferings = serviceOfferingService.getAllServicesByIds(ids);

        return ResponseEntity.ok(serviceOfferings);
    }
}
