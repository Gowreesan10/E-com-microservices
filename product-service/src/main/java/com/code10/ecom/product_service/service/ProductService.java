package com.code10.ecom.product_service.service;

import com.code10.ecom.product_service.Repository.ProductRepository;
import com.code10.ecom.product_service.client.InventoryClient;
import com.code10.ecom.product_service.dto.InventoryRequest;
import com.code10.ecom.product_service.dto.ProductRequest;
import com.code10.ecom.product_service.dto.ProductResponse;
import com.code10.ecom.product_service.model.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final InventoryClient inventoryClient;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();
        productRepository.save(product);
        log.info("Product created: {}", product);
        
        ProductResponse response = new ProductResponse(product.getSku_code(), product.getName(), product.getDescription(), product.getPrice());
        
        // Call inventory service via circuit breaker
        addInventoryRecord(product.getSku_code());
        
        return response;
    }

    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallbackAddInventory")
    private void addInventoryRecord(String skuCode) {
        InventoryRequest inventoryRequest = new InventoryRequest(skuCode, 0);
        inventoryClient.addInventory(inventoryRequest);
        log.info("Added inventory record for skuCode: {} with quantity: 0", skuCode);
    }

    private void fallbackAddInventory(String skuCode, Throwable throwable) {
        log.error("Circuit Breaker: Failed to create inventory record for skuCode: {}. Reason: {}", 
                skuCode, throwable.getMessage());
        // You could also add this to a retry queue or a dead letter topic here
    }

    public Optional<ProductResponse> getProductBySkuCode(String skuCode) {
        return productRepository.findById(skuCode)
                .map(product -> new ProductResponse(product.getSku_code(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice()));
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(
                product -> new ProductResponse(product.getSku_code(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice())
        ).toList();
    }
}
