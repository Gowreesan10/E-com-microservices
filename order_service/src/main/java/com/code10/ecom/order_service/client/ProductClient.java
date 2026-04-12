package com.code10.ecom.order_service.client;

import com.code10.ecom.order_service.dto.ProductResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Optional;

@HttpExchange
public interface ProductClient {

    @GetExchange( value = "/api/product/{skuCode}")
    public Optional<ProductResponse> getProductBySkuCode(@PathVariable String skuCode);
}
