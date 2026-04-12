package com.code10.ecom.order_service.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface InventoryClient {

    @GetExchange( value = "/api/inventory")
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);
}
