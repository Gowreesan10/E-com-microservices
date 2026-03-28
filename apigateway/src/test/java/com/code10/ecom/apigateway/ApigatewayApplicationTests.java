package com.code10.ecom.apigateway;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
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

    @Test
    @Order(1)
    void testProductServiceCreateRoute() {
        String randomProductName = "Test Product " + random.nextInt(10000);
        double randomPrice = 10.0 + (random.nextDouble() * 990.0);
        
        String response = webTestClient.post()
                .uri("/ecom/product")
                .header("Content-Type", "application/json")
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
