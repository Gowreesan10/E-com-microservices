package com.code10.ecom.inventory_service.controller;

import com.code10.ecom.inventory_service.dto.InventoryRequest;
import com.code10.ecom.inventory_service.dto.InventoryResponse;
import com.code10.ecom.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity){
        boolean result = inventoryService.isInStock(skuCode, quantity);
        log.info("Inventory check completed - SKU: {}, Quantity: {}, In Stock: {}", skuCode, quantity, result);
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse addInventory(@Valid @RequestBody InventoryRequest request) {
        InventoryResponse result = inventoryService.addInventory(request);
        log.info("Inventory added successfully - SKU: {}, Quantity: {}", request.getSkuCode(), request.getQuantity());
        return result;
    }

}
