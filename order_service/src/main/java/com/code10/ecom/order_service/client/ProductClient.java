package com.code10.ecom.order_service.client;

import com.code10.ecom.order_service.dto.ProductResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Optional;

@HttpExchange
public interface ProductClient {

    Logger log = LoggerFactory.getLogger(ProductClient.class);

    @GetExchange( value = "/api/product/{skuCode}")
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallbackIsValidProduct")
    public Optional<ProductResponse> getProductBySkuCode(@PathVariable String skuCode);

    default void fallbackIsValidProduct(String skuCode, Throwable throwable) {
        log.error("Circuit Breaker: Failed to create check product for skuCode: {}. Reason: {}",
                skuCode, throwable.getMessage());
    }
}
