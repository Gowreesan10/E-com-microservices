package com.code10.ecom.order_service.service;

import com.code10.ecom.order_service.dto.OrderRequest;
import com.code10.ecom.order_service.dto.OrderResponse;
import com.code10.ecom.event.OrderPlacedEvent;
import com.code10.ecom.order_service.model.order;
import com.code10.ecom.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public OrderResponse placeorder(OrderRequest orderRequest){
        try {
            order order = new order();
            order.setSkuCode(orderRequest.skuCode());
            order.setPrice(orderRequest.price());
            order.setQuantity(orderRequest.quantity());
            orderRepository.save(order);

            OrderPlacedEvent orderPlacedEvent = OrderPlacedEvent.newBuilder()
                    .setOrderID(order.getOrderNumber())
                    .setEmail(orderRequest.userDetails().email())
                    .build();

            log.info("Start Publishing OrderPlacedEvent to Kafka - OrderID: {}, Email: {}", orderPlacedEvent.getOrderID(), orderPlacedEvent.getEmail());
            kafkaTemplate.send("order-placed-topic", orderPlacedEvent);
            log.info("End Publishing OrderPlacedEvent to Kafka - OrderID: {}, Email: {}", orderPlacedEvent.getOrderID(), orderPlacedEvent.getEmail());

            return new OrderResponse(order.getOrderNumber(), order.getSkuCode(), order.getPrice(), order.getQuantity(), "Order Placed Successfully");
        } catch (RuntimeException e) {
            return new OrderResponse(null, orderRequest.skuCode(), orderRequest.price(), orderRequest.quantity(), "Failed to place order: " + e.getMessage());
        }

    }
}
