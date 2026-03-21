package com.code10.ecom.product_service.Controller;


import com.code10.ecom.product_service.dto.ProductRequest;
import com.code10.ecom.product_service.dto.ProductResponse;
import com.code10.ecom.product_service.model.Product;
import com.code10.ecom.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @GetMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<ProductResponse> getProductBySkuCode(@PathVariable String skuCode) {
        return productService.getProductBySkuCode(skuCode);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getProducts() {
        return productService.getAllProducts();
    }
}