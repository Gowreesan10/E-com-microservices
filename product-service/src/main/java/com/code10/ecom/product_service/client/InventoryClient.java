package com.code10.ecom.product_service.client;

import com.code10.ecom.product_service.dto.InventoryRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface InventoryClient {

    Logger log = LoggerFactory.getLogger(InventoryClient.class);

    @PostExchange(value = "/api/inventory")
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallbackAddInventory")
    InventoryResponse addInventory(@RequestBody InventoryRequest request);
    
    record InventoryResponse(String skuCode, Integer quantity, String status) {}

    default void fallbackAddInventory(InventoryRequest request, Throwable throwable) {
        log.error("Circuit Breaker: Failed to create inventory record for skuCode: {}. Reason: {}",
                request.skuCode(), throwable.getMessage());
        // You could also add this to a retry queue or a dead letter topic here
    }
}
