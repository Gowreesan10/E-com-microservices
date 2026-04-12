package com.code10.ecom.order_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table (name ="t_orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderNumber;
    
    @NotNull(message = "SKU code is required")
    private String skuCode;
    
    @NotNull(message = "Price is required")
    private BigDecimal price;
    
    @NotNull(message = "Quantity is required")
    private Integer quantity;

}

