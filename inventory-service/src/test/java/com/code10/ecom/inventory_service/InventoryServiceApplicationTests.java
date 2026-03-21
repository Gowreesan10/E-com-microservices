package com.code10.ecom.inventory_service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.Assumptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.assertj.core.api.Assertions.assertThat;

import com.code10.ecom.inventory_service.dto.InventoryRequest;
import com.code10.ecom.inventory_service.dto.InventoryResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.DockerClientFactory;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class InventoryServiceApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    void checkDockerAvailable() {
        // Skip tests early when Docker/Testcontainers is not available to avoid confusing failures
        Assumptions.assumeTrue(DockerClientFactory.instance().isDockerAvailable(), "Docker is not available, skipping integration tests");
    }

    @AfterEach
    void cleanup() {
        // Remove any test-created inventory entries to keep tests isolated
        jdbcTemplate.update("DELETE FROM t_inventory WHERE sku_code LIKE ?", "test-sku-%");
    }

    @Test
    void contextLoads() {
    }

    @Test
    void givenSeededData_whenQueryingInventory_thenReturnsTrue() {
        String url = "http://localhost:" + port + "/api/inventory?skuCode=iphone_5&quantity=1";
        Boolean response = restTemplate.getForObject(url, Boolean.class);
        assertThat(response).isTrue();
    }

    @Test
    void givenNewInventory_whenPosting_thenCreatedAndQueryable() {
        String postUrl = "http://localhost:" + port + "/api/inventory";
        InventoryRequest request = new InventoryRequest();
        request.setSkuCode("test-sku-123");
        request.setQuantity(5);

        InventoryResponse created = restTemplate.postForObject(postUrl, request, InventoryResponse.class);
        assertThat(created).isNotNull();
        assertThat(created.getSkuCode()).isEqualTo("test-sku-123");
        assertThat(created.getQuantity()).isEqualTo(5);

        String getUrl = "http://localhost:" + port + "/api/inventory?skuCode=test-sku-123&quantity=1";
        Boolean inStock = restTemplate.getForObject(getUrl, Boolean.class);
        assertThat(inStock).isTrue();
    }

}
