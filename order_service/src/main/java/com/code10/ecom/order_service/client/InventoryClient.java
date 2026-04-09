package com.code10.ecom.order_service.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "http://localhost:8082/")
public interface InventoryClient {

    @HttpExchange( value = "/api/inventory", method = "GET")
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);
}
