package com.code10.ecom.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) throws Exception {
        return serverHttpSecurity.authorizeExchange(authorize -> authorize.anyExchange().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt())
                .build();
    }

}
