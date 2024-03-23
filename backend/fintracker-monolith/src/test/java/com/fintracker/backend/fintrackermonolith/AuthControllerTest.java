package com.fintracker.backend.fintrackermonolith;

import com.fintracker.backend.fintrackermonolith.auth_server.db.repository.UserRepository;
import com.fintracker.backend.fintrackermonolith.auth_server.module.auth.api.request.LoginRequest;
import com.fintracker.backend.fintrackermonolith.auth_server.module.auth.api.request.SignUpRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        userRepository.deleteAll();
        SignUpRequest request = new SignUpRequest(
                "user",
                "user@mail.com",
                "pass"
        );
//        given()
//                .contentType(ContentType.JSON)
//                .disableCsrf()
//                .body(request)
//                .when()
//                .post("/auth/register")
//                .then()
//                .statusCode(200);
    }

    @Test
    void testRegisterSuccess() {
        SignUpRequest request = new SignUpRequest(
                "user2",
                "user2@mail.com",
                "pass2"
        );
        given()
                .contentType(ContentType.JSON)
                .disableCsrf()
                .body(request)
                .when()
                .post("/auth/signup")
                .then()
                .statusCode(200);

        var user = userRepository.findUserByUsernameOrEmail("user2").get();
        assertEquals("user2", user.getUsername());
    }

    @Test
    void testLoginSuccess() {
        testRegisterSuccess();
        LoginRequest loginRequest = new LoginRequest(
                "user2",
                "pass2"
        );
        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200);
    }
}
