package com.code10.ecom.inventory_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryRequest {

    @NotBlank
    private String skuCode;

    @NotNull
    @Min(0)
    private Integer quantity;

}

