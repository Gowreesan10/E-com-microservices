package com.code10.ecom.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "inventory-service", url = "http://localhost:8082/")
public interface InventoryClient {

    @GetMapping("/api/inventory")
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);
}
