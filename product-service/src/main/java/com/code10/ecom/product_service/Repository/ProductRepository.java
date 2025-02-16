package com.code10.ecom.product_service.Repository;

import com.code10.ecom.product_service.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product,String>{
}