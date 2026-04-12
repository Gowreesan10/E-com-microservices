package com.code10.ecom.inventory_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "t_inventory")
public class Inventory {

    @Id
    @NotBlank(message = "SKU code is required")
    @Column(nullable = false)
    private String skuCode;
    
    @NotNull(message = "Quantity is required")
    private Integer quantity;
}
