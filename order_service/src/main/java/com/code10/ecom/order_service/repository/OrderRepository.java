package com.code10.ecom.order_service.repository;

import com.code10.ecom.order_service.model.order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<order,Long> {

}
