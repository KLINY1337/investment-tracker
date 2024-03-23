package com.fintracker.backend.fintrackermonolith;

import com.fintracker.backend.fintrackermonolith.auth_server.db.repository.UserRepository;
import com.fintracker.backend.fintrackermonolith.auth_server.module.auth.api.request.SignUpRequest;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Asset;
import com.fintracker.backend.fintrackermonolith.core.db.repository.AssetRepository;
import com.fintracker.backend.fintrackermonolith.core.module.asset.api.request.CreateAssetRequest;
import com.fintracker.backend.fintrackermonolith.core.module.asset.api.request.UpdateAssetByIdRequest;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssetControllerTest {

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
    AssetRepository assetRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        assetRepository.deleteAll();
    }

    @Test
    void testCreateAssetSuccess() {
        CreateAssetRequest request = new CreateAssetRequest(
                "BTC",
                "CRYPTO"
        );
        given()
                .contentType(ContentType.JSON)
                .disableCsrf()
                .body(request)
                .when()
                .post("/api/v1/assets")
                .then()
                .statusCode(200);
    }

    @Test
    void testGetAssetsByIdsSuccess() {
        Asset asset1 = assetRepository.save(new Asset(
                null,
                "BTC",
                "CRYPTO"
        ));
        Asset asset2 = assetRepository.save(new Asset(
                null,
                "ETH",
                "CRYPTO"
        ));
        given()
                .contentType(ContentType.JSON)
                .disableCsrf()
                .param("idList", asset1.getId() + "," + asset2.getId())
                .when()
                .get("/api/v1/assets")
                .then()
                .statusCode(200);
    }

    @Test
    void testUpdateAssetByIdSuccess() {
        Asset asset1 = assetRepository.save(new Asset(
                null,
                "ETH",
                "CRYPTO"
        ));

        UpdateAssetByIdRequest request = new UpdateAssetByIdRequest(
                "WTF",
                "CRYPTO"
        );
        given()
                .contentType(ContentType.JSON)
                .disableCsrf()
                .param("id", asset1.getId())
                .body(request)
                .when()
                .put("/api/v1/assets")
                .then()
                .statusCode(200);
    }

    @Test
    void testDeleteAssetsByIdsSuccess() {
        Asset asset1 = assetRepository.save(new Asset(
                null,
                "ETH",
                "CRYPTO"
        ));
        Asset asset2 = assetRepository.save(new Asset(
                null,
                "GPH",
                "CRYPTO"
        ));
        given()
                .contentType(ContentType.JSON)
                .disableCsrf()
                .param("idList", asset1.getId() + "," + asset2.getId())
                .when()
                .delete("/api/v1/assets")
                .then()
                .statusCode(200);
    }
}
