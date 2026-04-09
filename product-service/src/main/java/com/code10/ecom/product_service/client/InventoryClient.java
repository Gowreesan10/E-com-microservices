package com.code10.ecom.product_service.client;

import com.code10.ecom.product_service.dto.InventoryRequest;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "http://localhost:8082/")
public interface InventoryClient {

    @PostExchange(value = "/api/inventory")
    InventoryResponse addInventory(InventoryRequest request);
    
    record InventoryResponse(String skuCode, Integer quantity, String status) {}
}
