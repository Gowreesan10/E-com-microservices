package com.code10.ecom.order_service.config;

import com.code10.ecom.order_service.client.InventoryClient;
import com.code10.ecom.order_service.client.ProductClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {

    @Bean
    public InventoryClient inventoryClient(){
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();

        return httpServiceProxyFactory.createClient(InventoryClient.class);
    }

    @Bean
    public ProductClient productClient(){
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();
        return httpServiceProxyFactory.createClient(ProductClient.class);
    }
}
