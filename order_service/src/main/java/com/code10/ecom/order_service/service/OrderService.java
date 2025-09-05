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
        order orderrequest = new order();
        orderrequest.setOrderNumber(UUID.randomUUID().toString());
        orderrequest.setSkuCode(orderRequest.skuCode());
        orderrequest.setPrice(orderRequest.price());
        orderrequest.setQuantity(orderRequest.quantity());
        orderRepository.save(orderrequest);
    }
}
