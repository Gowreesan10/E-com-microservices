package com.code10.ecom.product_service.service;

import com.code10.ecom.product_service.Repository.ProductRepository;
import com.code10.ecom.product_service.client.InventoryClient;
import com.code10.ecom.product_service.dto.InventoryRequest;
import com.code10.ecom.product_service.dto.ProductRequest;
import com.code10.ecom.product_service.dto.ProductResponse;
import com.code10.ecom.product_service.model.Product;
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
        
        // Add product to inventory with zero quantity
        try {
            InventoryRequest inventoryRequest = new InventoryRequest(product.getSku_code(), 0);
            inventoryClient.addInventory(inventoryRequest);
            log.info("Added inventory record for skuCode: {} with quantity: 0", product.getSku_code());
        } catch (Exception e) {
            log.error("Failed to create inventory record for skuCode: {}", product.getSku_code(), e);
        }
        
        return response;
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
