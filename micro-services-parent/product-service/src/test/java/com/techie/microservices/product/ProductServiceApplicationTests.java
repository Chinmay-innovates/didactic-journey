package com.techie.microservices.product;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestcontainersConfiguration.class)
class ProductServiceApplicationTests {
    @LocalServerPort
    private Integer port;

    @AfterAll
    static void tearDown() {
        TestcontainersConfiguration.mongoDbContainer().stop();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldCreateProduct() {
        String requestBody = """
                {
                    "name": "iPhone 15 Red",
                    "description": "Latest Apple smartphone in Red",
                    "price": 1200
                }
                """;
        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("iPhone 15 Red"))
                .body("description", Matchers.equalTo("Latest Apple smartphone in Red"))
                .body("price", Matchers.equalTo(1200));
    }

    @Test
    void shouldGetAllProducts() {
        // Ensure at least one product exists
        shouldCreateProduct();

        given()
                .when()
                .get("/api/product")
                .then()
                .statusCode(200)
                .body("$", Matchers.not(Matchers.empty()))
                .body("[0].id", Matchers.notNullValue())
                .body("[0].name", Matchers.notNullValue())
                .body("[0].description", Matchers.notNullValue())
                .body("[0].price", Matchers.greaterThan(0));
    }
}
