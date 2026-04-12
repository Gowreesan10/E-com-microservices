package com.code10.ecom.order_service.controller;

import com.code10.ecom.order_service.client.InventoryClient;
import com.code10.ecom.order_service.client.ProductClient;
import com.code10.ecom.order_service.dto.OrderRequest;
import com.code10.ecom.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final InventoryClient inventoryClient;
    private final ProductClient productClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        // Validate product exists
        validateProductExists(orderRequest.skuCode());

        // Validate inventory
        boolean isProductInStock = checkProductInStock(orderRequest.skuCode(), orderRequest.quantity());
        if (isProductInStock) {
            orderService.placeorder(orderRequest);
            String response = "Order Placed Successfully";
            log.info("Order placed successfully - SKU: {}, Quantity: {}", orderRequest.skuCode(), orderRequest.quantity());
            return response;
        } else {
            String errorMsg = "Product with skuCode " + orderRequest.skuCode() + " is not in stock";
            log.error("Order failed - SKU: {}, Quantity: {}, Reason: {}", orderRequest.skuCode(), orderRequest.quantity(), errorMsg);
            throw new RuntimeException(errorMsg);
        }
    }

    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallbackIsValidProduct")
    public void validateProductExists(String skuCode){
        productClient.getProductBySkuCode(skuCode).orElseThrow(() -> new RuntimeException("Product with skuCode " + skuCode + " not found"));
    }

    public void fallbackIsValidProduct(String skuCode, Throwable throwable) {
        log.error("Circuit Breaker: Failed to create check product for skuCode: {}. Reason: {}",
                skuCode, throwable.getMessage());
    }

    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallbackProductInStock")
    public boolean checkProductInStock(String skuCode, Integer quantity){
        return inventoryClient.isInStock(skuCode, quantity);
    }

    public boolean fallbackProductInStock(String skuCode, Integer quantity, Throwable throwable) {
        log.error("Circuit Breaker: Failed to check inventory for skuCode: {}, quantity: {}. Reason: {}",
                skuCode, quantity, throwable.getMessage());
        return false; // Assume not in stock if inventory service is down
    }

}
