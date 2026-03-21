package com.code10.ecom.order_service.service;

import com.code10.ecom.order_service.dto.OrderRequest;
import com.code10.ecom.order_service.dto.OrderResponse;
import com.code10.ecom.order_service.model.order;
import com.code10.ecom.order_service.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponse placeorder(OrderRequest orderRequest){
        try {
            order order = new order();
            order.setSkuCode(orderRequest.skuCode());
            order.setPrice(orderRequest.price());
            order.setQuantity(orderRequest.quantity());
            orderRepository.save(order);

            return new OrderResponse(order.getOrderNumber(), order.getSkuCode(), order.getPrice(), order.getQuantity(), "Order Placed Successfully");
        } catch (RuntimeException e) {
            return new OrderResponse(null, orderRequest.skuCode(), orderRequest.price(), orderRequest.quantity(), "Failed to place order: " + e.getMessage());
        }

    }
}
