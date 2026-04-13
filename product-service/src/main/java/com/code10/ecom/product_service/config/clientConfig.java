package com.code10.ecom.product_service.config;

import com.code10.ecom.product_service.client.InventoryClient;
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
public class clientConfig {

    @Value("${inventory-url}")
    private String inventoryUrl;

    @Bean
    InventoryClient inventoryClient() {

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // Connection timeout
                .responseTimeout(java.time.Duration.ofSeconds(5)); // Response timeout

        WebClient webclient = WebClient.builder()
                .baseUrl(inventoryUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webclient))
                .build();
        return httpServiceProxyFactory.createClient(InventoryClient.class);
    }
}
