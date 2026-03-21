package com.code10.ecom.order_service.dto;

import java.math.BigDecimal;

public record ProductResponse(String skuCode, String name, String description, BigDecimal price) {
}
