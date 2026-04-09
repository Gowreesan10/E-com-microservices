package com.code10.ecom.product_service.client;

import com.code10.ecom.product_service.dto.InventoryRequest;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "http://localhost:8082/")
public interface InventoryClient {

    @HttpExchange(value = "/api/inventory", method = "POST")
    InventoryResponse addInventory(InventoryRequest request);
    
    record InventoryResponse(String skuCode, Integer quantity, String status) {}
}
