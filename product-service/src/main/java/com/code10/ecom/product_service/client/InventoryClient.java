package com.code10.ecom.product_service.client;

import com.code10.ecom.product_service.dto.InventoryRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface InventoryClient {

    @PostExchange(value = "/api/inventory")
    InventoryResponse addInventory(@RequestBody InventoryRequest request);
    
    record InventoryResponse(String skuCode, Integer quantity, String status) {}
}
