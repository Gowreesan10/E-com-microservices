package com.code10.ecom.order_service.controller;

import com.code10.ecom.order_service.client.InventoryClient;
import com.code10.ecom.order_service.client.ProductClient;
import com.code10.ecom.order_service.dto.OrderRequest;
import com.code10.ecom.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final InventoryClient inventoryClient;
    private final ProductClient productClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        // Validate product exists
        var product = productClient.getProductBySkuCode(orderRequest.skuCode())
                .orElseThrow(() -> new RuntimeException("Product with skuCode " + orderRequest.skuCode() + " not found"));
        
        // Validate inventory
        boolean isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if(isProductInStock){
            orderService.placeorder(orderRequest);
            return "Order Placed Successfully";
        }else {
            throw new RuntimeException("Product with skuCode " + orderRequest.skuCode() + " is not in stock");
        }
    }


}
