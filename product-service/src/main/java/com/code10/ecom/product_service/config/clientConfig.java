package com.code10.ecom.product_service.config;

import com.code10.ecom.product_service.client.InventoryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class clientConfig {

    @Bean
    InventoryClient inventoryClient() {
        WebClient webclient = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webclient))
                .build();
        return httpServiceProxyFactory.createClient(InventoryClient.class);
    }
}
