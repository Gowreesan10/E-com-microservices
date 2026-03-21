package com.code10.ecom.product_service.client;

import com.code10.ecom.product_service.dto.InventoryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "inventory-service", url = "http://localhost:8082/")
public interface InventoryClient {

    @PostMapping("/api/inventory")
    InventoryResponse addInventory(InventoryRequest request);
    
    record InventoryResponse(String skuCode, Integer quantity, String status) {}
}
