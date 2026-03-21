-- creates table with all constraints and initial data
CREATE TABLE t_inventory (
    sku_code VARCHAR(255) NOT NULL,
    quantity INT(11),
    PRIMARY KEY (sku_code)
);

-- Add index on sku_code for better query performance
CREATE INDEX idx_inventory_sku_code ON t_inventory(sku_code);