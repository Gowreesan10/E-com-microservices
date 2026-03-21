CREATE TABLE t_orders (
    order_number BIGINT(20) NOT NULL AUTO_INCREMENT,
    sku_code VARCHAR(255),
    price DECIMAL(19,2),
    quantity INT(11),
    PRIMARY KEY (order_number)
);