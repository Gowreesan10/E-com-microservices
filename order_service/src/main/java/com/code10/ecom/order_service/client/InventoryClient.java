package com.code10.ecom.order_service.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface InventoryClient {

    Logger log = LoggerFactory.getLogger(InventoryClient.class);

    @GetExchange( value = "/api/inventory")
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallbackProductInStock")
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);


    default boolean fallbackProductInStock(String skuCode, Integer quantity, Throwable throwable) {
        log.error("Circuit Breaker: Failed to check inventory for skuCode: {}, quantity: {}. Reason: {}",
                skuCode, quantity, throwable.getMessage());
        return false; // Assume not in stock if inventory service is down
    }

}
