package com.fintracker.backend.fintrackermonolith;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;
import com.fintracker.backend.fintrackermonolith.auth_server.db.repository.UserRepository;
import com.fintracker.backend.fintrackermonolith.auth_server.module.auth.api.request.SignUpRequest;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Asset;
import com.fintracker.backend.fintrackermonolith.core.db.entity.InvestmentPosition;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Portfolio;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Ticker;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.DenominationType;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.ExpirationType;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.MarketType;
import com.fintracker.backend.fintrackermonolith.core.db.repository.AssetRepository;
import com.fintracker.backend.fintrackermonolith.core.db.repository.InvestmentPositionRepository;
import com.fintracker.backend.fintrackermonolith.core.db.repository.PortfolioRepository;
import com.fintracker.backend.fintrackermonolith.core.db.repository.TickerRepository;
import com.fintracker.backend.fintrackermonolith.core.module.asset.api.request.UpdateAssetByIdRequest;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.request.CreateInvestmentPositionRequest;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.request.UpdateInvestmentPositionByIdRequest;
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

import java.math.BigDecimal;
import java.util.Date;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InvestmentPositionControllerTest {

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
    private InvestmentPositionRepository investmentPositionRepository;
    @Autowired
    private TickerRepository tickerRepository;
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        investmentPositionRepository.deleteAll();
        tickerRepository.deleteAll();
        portfolioRepository.deleteAll();
        assetRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testCreateInvestmentPositionSuccess() throws InterruptedException {
        Asset baseAsset = assetRepository.save(new Asset(
                null,
                "BTC",
                "CRYPTO"
        ));
        Asset quoteAsset = assetRepository.save(new Asset(
                null,
                "USDT",
                "CRYPTO"
        ));
        Ticker ticker = tickerRepository.save(new Ticker(
                null,
                "BTCUSDT",
                "BTCUSDT",
                "binance",
                baseAsset,
                quoteAsset,
                DenominationType.VANILLA,
                ExpirationType.PERPETUAL,
                MarketType.FUTURE,
                true
        ));

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

        Portfolio portfolio = portfolioRepository.save(new Portfolio(
                null,
                userRepository.findUserByUsernameOrEmail("user2").get(),
                "portfolio name"
        ));
        CreateInvestmentPositionRequest investmentPositionRequest = new CreateInvestmentPositionRequest(
                ticker.getId(),
                portfolio.getId(),
                new Date(),
                null,
                BigDecimal.ONE,
                BigDecimal.TWO,
                BigDecimal.ONE
        );
        Thread.sleep(100);

        given()
                .contentType(ContentType.JSON)
                .disableCsrf()
                .body(investmentPositionRequest)
                .when()
                .post("/api/v1/investment_positions")
                .then()
                .statusCode(200);
    }

    @Test
    void testGetInvestmentPositionsByIdsSuccess() throws InterruptedException {
        Asset baseAsset = assetRepository.save(new Asset(
                null,
                "BTC",
                "CRYPTO"
        ));
        Asset quoteAsset = assetRepository.save(new Asset(
                null,
                "USDT",
                "CRYPTO"
        ));
        Ticker ticker = tickerRepository.save(new Ticker(
                null,
                "BTCUSDT",
                "BTCUSDT",
                "binance",
                baseAsset,
                quoteAsset,
                DenominationType.VANILLA,
                ExpirationType.PERPETUAL,
                MarketType.FUTURE,
                true
        ));

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

        Portfolio portfolio = portfolioRepository.save(new Portfolio(
                null,
                userRepository.findUserByUsernameOrEmail("user2").get(),
                "portfolio name"
        ));
        CreateInvestmentPositionRequest investmentPositionRequest = new CreateInvestmentPositionRequest(
                ticker.getId(),
                portfolio.getId(),
                new Date(),
                null,
                BigDecimal.ONE,
                BigDecimal.TWO,
                BigDecimal.ONE
        );
        Thread.sleep(100);

        given()
                .contentType(ContentType.JSON)
                .disableCsrf()
                .body(investmentPositionRequest)
                .when()
                .post("/api/v1/investment_positions")
                .then()
                .statusCode(200);

        InvestmentPosition investmentPosition = investmentPositionRepository.findAll().get(0);
        given()
                .contentType(ContentType.JSON)
                .disableCsrf()
                .param("idList", investmentPosition.getId().toString())
                .when()
                .get("/api/v1/investment_positions/byIds")
                .then()
                .statusCode(200);
    }

    @Test
    void testUpdateInvestmentPositionByIdSuccess() throws InterruptedException {
        Asset baseAsset = assetRepository.save(new Asset(
                null,
                "BTC",
                "CRYPTO"
        ));
        Asset quoteAsset = assetRepository.save(new Asset(
                null,
                "USDT",
                "CRYPTO"
        ));
        Ticker ticker = tickerRepository.save(new Ticker(
                null,
                "BTCUSDT",
                "BTCUSDT",
                "binance",
                baseAsset,
                quoteAsset,
                DenominationType.VANILLA,
                ExpirationType.PERPETUAL,
                MarketType.FUTURE,
                true
        ));

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

        Portfolio portfolio = portfolioRepository.save(new Portfolio(
                null,
                userRepository.findUserByUsernameOrEmail("user2").get(),
                "portfolio name"
        ));
        CreateInvestmentPositionRequest investmentPositionRequest = new CreateInvestmentPositionRequest(
                ticker.getId(),
                portfolio.getId(),
                new Date(),
                null,
                BigDecimal.ONE,
                BigDecimal.TWO,
                BigDecimal.ONE
        );
        Thread.sleep(100);

        given()
                .contentType(ContentType.JSON)
                .disableCsrf()
                .body(investmentPositionRequest)
                .when()
                .post("/api/v1/investment_positions")
                .then()
                .statusCode(200);

        InvestmentPosition investmentPosition = investmentPositionRepository.findAll().get(0);
        UpdateInvestmentPositionByIdRequest updateInvestmentPositionByIdRequest = new UpdateInvestmentPositionByIdRequest(
                ticker.getId(),
                portfolio.getId(),
                new Date(),
                null,
                BigDecimal.TEN,
                BigDecimal.TWO,
                BigDecimal.TEN,
                BigDecimal.TWO,
                BigDecimal.TEN
        );
        given()
                .contentType(ContentType.JSON)
                .disableCsrf()
                .param("id", investmentPosition.getId().toString())
                .body(updateInvestmentPositionByIdRequest)
                .when()
                .put("/api/v1/investment_positions")
                .then()
                .statusCode(200);
    }

//    @Test
//    void testDeleteAssetsByIdsSuccess() {
//        Asset asset1 = tickerRepository.save(new Asset(
//                null,
//                "ETH",
//                "CRYPTO"
//        ));
//        Asset asset2 = tickerRepository.save(new Asset(
//                null,
//                "GPH",
//                "CRYPTO"
//        ));
//        given()
//                .contentType(ContentType.JSON)
//                .disableCsrf()
//                .param("idList", asset1.getId() + "," + asset2.getId())
//                .when()
//                .delete("/api/v1/investment_positions")
//                .then()
//                .statusCode(200);
//    }
}
