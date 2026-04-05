package com.code10.ecom.apigateway;

import lombok.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Random;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApigatewayApplicationTests {

    @Autowired
    private WebTestClient webTestClient;
    
    private static String productSku = "";
    private final Random random = new Random();
    private String accessToken = "";
    private final String client_id = "testuser";
    private final String client_secret = "CODE1010";



    @BeforeEach
    void setup() {
        fetchToken();
    }

    private void fetchToken() {

        WebTestClient keycloakClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:9001/")
                .build();

        accessToken = keycloakClient.post()
                .uri("realms/Security-Ecom/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("grant_type=client_credentials&client_id=%s&client_secret=%s".formatted(client_id, client_secret))
                .exchange()
                .expectStatus().isOk()
                .returnResult(TokenResponse.class)
                .getResponseBody()
                .blockFirst()
                .getAccess_token();

        System.out.println("Fetched access token: " + accessToken);
    }

    @Getter
    @Setter
    private static class TokenResponse {
        private String access_token;
    }

    @Test
    @Order(0)
    void testInvalidToken() {
        String randomProductName = "Test Product " + random.nextInt(10000);
        double randomPrice = 10.0 + (random.nextDouble() * 990.0);

        FluxExchangeResult<String> response = webTestClient.post()
                .uri("/ecom/product")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + "12345678910")
                .bodyValue("{\"name\":\"" + randomProductName + "\",\"description\":\"Test Description\",\"price\":" + String.format("%.2f", randomPrice) + "}")
                .exchange()
                .expectStatus().isUnauthorized()
                .returnResult(String.class);

        System.out.println("Product creation with Invalid token response: " + response);
    }

    @Test
    @Order(1)
    void testProductServiceCreateRoute() {
        String randomProductName = "Test Product " + random.nextInt(10000);
        double randomPrice = 10.0 + (random.nextDouble() * 990.0);
        
        String response = webTestClient.post()
                .uri("/ecom/product")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue("{\"name\":\"" + randomProductName + "\",\"description\":\"Test Description\",\"price\":" + String.format("%.2f", randomPrice) + "}")
                .exchange()
                .expectStatus().isCreated()
                .returnResult(String.class)
                .getResponseBody()
                .blockFirst();

        System.out.println("Product creation response: " + response);
        if (response != null && response.contains("skuCode")) {
            productSku = extractSkuFromResponse(response);
            System.out.println("Extracted SKU: " + productSku);
        } else {
            System.out.println("No skuCode found in response, using fallback");
        }
    }

    @Test
    @Order(2)
    void testProductServiceGetAllRoute() {
        String response = webTestClient.get()
                .uri("/ecom/product")
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody()
                .blockFirst();

        System.out.println("Get all products response: " + response);
    }

    @Test
    @Order(3)
    void testProductServiceGetBySkuRoute() {
        System.out.println("Using SKU for lookup: " + productSku);
        String requestUri = "/ecom/product/" + productSku;
        System.out.println("Request URI: " + requestUri);
        
        String response = webTestClient.get()
                .uri(requestUri)
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody()
                .blockFirst();

        System.out.println("Product lookup response: " + response);
    }

    @Test
    @Order(4)
    void testInventoryServiceDefaultQuantityCheck() {
        String response = webTestClient.get()
                .uri("/ecom/inventory?skuCode=" + productSku + "&quantity=0")
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody()
                .blockFirst();

        System.out.println("Inventory default quantity check response: " + response);
    }

    @Test
    @Order(5)
    void testInventoryServiceAddRoute() {
        String response = webTestClient.post()
                .uri("/ecom/inventory")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue("{\"skuCode\":\"" + productSku + "\",\"quantity\":100}")
                .exchange()
                .expectStatus().isCreated()
                .returnResult(String.class)
                .getResponseBody()
                .blockFirst();

        System.out.println("Inventory addition response: " + response);
    }

    @Test
    @Order(6)
    void testInventoryServiceCheckStockRoute() {
        String response = webTestClient.get()
                .uri("/ecom/inventory?skuCode=" + productSku + "&quantity=100")
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody()
                .blockFirst();

        System.out.println("Inventory stock check response: " + response);
    }

    @Test
    @Order(7)
    void testOrderServicePlaceOrderRoute() {
        String response = webTestClient.post()
                .uri("/ecom/order")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue("{\"skuCode\":\"" + productSku + "\",\"quantity\":100}")
                .exchange()
                .expectStatus().isCreated()
                .returnResult(String.class)
                .getResponseBody()
                .blockFirst();

        System.out.println("Order placement response: " + response);
    }

    @Test
    void testInvalidRouteReturns404() {
        String response = webTestClient.get()
                .uri("/ecom/invalid-route")
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isNotFound()
                .returnResult(String.class)
                .getResponseBody()
                .blockFirst();

        System.out.println("Invalid route response: " + response);
    }

    @Test
    void testCorsHeaders() {
        String response = webTestClient.options()
                .uri("/ecom/product")
                .header("Origin", "http://localhost:3000")
                .header("Authorization", "Bearer " + accessToken)
                .header("Access-Control-Request-Method", "GET")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().exists("Access-Control-Allow-Origin")
                .returnResult(String.class)
                .getResponseBody()
                .blockFirst();

        System.out.println("CORS headers response: " + response);
    }

    private String extractSkuFromResponse(String response) {
        try {
            int skuIndex = response.indexOf("\"skuCode\":\"");
            if (skuIndex != -1) {
                int start = skuIndex + 11;
                int end = response.indexOf("\"", start);
                return response.substring(start, end);
            }
        } catch (Exception e) {
            System.out.println("Error extracting SKU: " + e.getMessage());
        }
        return "TEST-PRODUCT-123";
    }
}
