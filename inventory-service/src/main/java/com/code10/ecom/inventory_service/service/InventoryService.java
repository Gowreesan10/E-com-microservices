package com.code10.ecom.inventory_service.service;

import com.code10.ecom.inventory_service.dto.InventoryRequest;
import com.code10.ecom.inventory_service.dto.InventoryResponse;
import com.code10.ecom.inventory_service.model.Inventory;
import com.code10.ecom.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode, Integer quantity) {
        return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
    }

    public InventoryResponse addInventory(InventoryRequest request) {
        Inventory inventory = new Inventory();
        inventory.setSkuCode(request.getSkuCode());
        inventory.setQuantity(request.getQuantity());
        Inventory saved = inventoryRepository.save(inventory);
        return new InventoryResponse(saved.getId(), saved.getSkuCode(), saved.getQuantity());
    }

}
