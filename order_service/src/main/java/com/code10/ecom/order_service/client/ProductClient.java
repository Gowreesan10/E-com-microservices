package com.code10.ecom.order_service.client;

import com.code10.ecom.order_service.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(value = "product-service", url = "http://localhost:8080/")
public interface ProductClient {

    @GetMapping("/api/product/{skuCode}")
    public Optional<ProductResponse> getProductBySkuCode(@PathVariable String skuCode);
}
