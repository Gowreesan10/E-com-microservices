package com.code10.ecom.order_service.dto;

import java.math.BigDecimal;

public record OrderRequest(
         String skuCode,
         BigDecimal price,
         Integer quantity,
         UserDetails userDetails) {

    public record UserDetails(
            String name,
            String email,
            String address
    ){}

}
