package com.code10.ecom.order_service.service;

import com.code10.ecom.order_service.dto.OrderRequest;
import com.code10.ecom.order_service.model.order;
import com.code10.ecom.order_service.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeorder(OrderRequest orderRequest){
        order order = new order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setSkuCode(orderRequest.skuCode());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        orderRepository.save(order);
    }
}
