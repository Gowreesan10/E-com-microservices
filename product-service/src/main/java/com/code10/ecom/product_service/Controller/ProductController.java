package com.code10.ecom.product_service.Controller;


import com.code10.ecom.product_service.dto.ProductRequest;
import com.code10.ecom.product_service.dto.ProductResponse;
import com.code10.ecom.product_service.model.Product;
import com.code10.ecom.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse result = productService.createProduct(productRequest);
        log.info("Product created successfully - SKU: {}, Name: {}, Price: {}", result.skuCode(), result.name(), result.price());
        return result;
    }

    @GetMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<ProductResponse> getProductBySkuCode(@PathVariable String skuCode) {
        Optional<ProductResponse> result = productService.getProductBySkuCode(skuCode);
        if (result.isPresent()) {
            log.info("Product retrieved successfully - SKU: {}, Name: {}", skuCode, result.get().name());
        } else {
            log.warn("Product not found - SKU: {}", skuCode);
        }
        return result;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getProducts() {
        List<ProductResponse> result = productService.getAllProducts();
        log.info("Retrieved {} products successfully", result.size());
        return result;
    }
}