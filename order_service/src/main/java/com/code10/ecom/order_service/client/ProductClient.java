package com.code10.ecom.order_service.client;

import com.code10.ecom.order_service.dto.ProductResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Optional;

@HttpExchange(url = "http://localhost:8080/")
public interface ProductClient {

    @HttpExchange( value = "/api/product/{skuCode}", method = "GET")
    public Optional<ProductResponse> getProductBySkuCode(@PathVariable String skuCode);
}
