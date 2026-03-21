package com.code10.ecom.order_service.dto;

import java.math.BigDecimal;

public record OrderResponse(
        Long orderNumber,
        String skuCode,
        BigDecimal price,
        Integer quantity,
        String status) {
}
