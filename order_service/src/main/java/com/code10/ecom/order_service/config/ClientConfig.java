package com.code10.ecom.order_service.config;

import com.code10.ecom.order_service.client.InventoryClient;
import com.code10.ecom.order_service.client.ProductClient;
import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

@Configuration
public class ClientConfig {

    @Value("${inventory-url}")
    private String inventoryUrl;
    @Value("${product-url}")
    private String productUrl;

    @Bean
    public InventoryClient inventoryClient(){

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // Connection timeout
                .responseTimeout(java.time.Duration.ofSeconds(5)); // Response timeout

        WebClient webClient = WebClient.builder()
                .baseUrl(inventoryUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();

        return httpServiceProxyFactory.createClient(InventoryClient.class);
    }

    @Bean
    public ProductClient productClient(){
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // Connection timeout
                .responseTimeout(java.time.Duration.ofSeconds(5)); // Response timeout

        WebClient webClient = WebClient.builder()
                .baseUrl(productUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();
        return httpServiceProxyFactory.createClient(ProductClient.class);
    }
}
