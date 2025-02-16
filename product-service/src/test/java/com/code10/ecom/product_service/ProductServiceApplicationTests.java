package com.code10.ecom.product_service;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:latest");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup(){
		//The error java.net.MalformedURLException: Error at index 0 in: ":4799" indicates that the URL being constructed is missing the hostname part.
		RestAssured.baseURI = "http://localhost:" + port;
	}

	static {
		mongoDbContainer.start();
	}

	@Test
	void shouldCreateProduct() {
		String reqBody = """
		{
			"name": "i phone 16",
			"description": "A mobile by Apple",
			"price": 10000
		}
		""";
		//The error Expected status code <201> but was <415> indicates that the server is responding with a 415 Unsupported Media Type status code. This typically means that the server does not understand the format of the request payload.
		RestAssured.given()
				.contentType("application/json")
				.body(reqBody)
				.post("api/product")
				.then()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("i phone 16"))
				.body("description", Matchers.equalTo("A mobile by Apple"))
				.body("price", Matchers.equalTo(10000));
	}

}
