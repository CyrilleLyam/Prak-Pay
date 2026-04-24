package com.seanglay.gatling.simulation;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class TransactionSimulation extends Simulation {

    private static final String BASE_URL = System.getProperty("baseUrl", "http://localhost:8080");
    private static final String WALLET_ID = System.getProperty("walletId", "a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    private static final String RECEIVER_WALLET_ID = System.getProperty("receiverWalletId", "b2c3d4e5-f6a7-8901-bcde-f12345678901");

    HttpProtocolBuilder httpProtocol = http.baseUrl(BASE_URL)
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    ChainBuilder login = exec(
            http("Login").post("/api/v1/accounts/login")
                    .body(StringBody("""
                            {
                              "email": "seed@test.com",
                              "password": "Test@1234"
                            }
                            """))
                    .check(status().is(200))
                    .check(jsonPath("$.data.access_token").saveAs("token"))
    );

    ChainBuilder deposit = exec(
            http("Deposit").post("/api/v1/transactions")
                    .header("Authorization", "Bearer #{token}")
                    .body(StringBody("""
                            {
                              "walletId": "%s",
                              "amount": 100.00,
                              "type": "DEPOSIT"
                            }
                            """.formatted(WALLET_ID)))
                    .check(status().is(201))
    );

    ChainBuilder withdraw = exec(
            http("Withdraw").post("/api/v1/transactions")
                    .header("Authorization", "Bearer #{token}")
                    .body(StringBody("""
                            {
                              "walletId": "%s",
                              "amount": 10.00,
                              "type": "WITHDRAW"
                            }
                            """.formatted(WALLET_ID)))
                    .check(status().is(201))
    );

    ChainBuilder transfer = exec(
            http("Transfer").post("/api/v1/transactions")
                    .header("Authorization", "Bearer #{token}")
                    .body(StringBody("""
                            {
                              "walletId": "%s",
                              "amount": 5.00,
                              "type": "TRANSFER_OUT",
                              "relatedWalletId": "%s"
                            }
                            """.formatted(WALLET_ID, RECEIVER_WALLET_ID)))
                    .check(status().is(201))
    );

    ScenarioBuilder depositScenario = scenario("Deposit Scenario")
            .exec(login)
            .exec(deposit);

    ScenarioBuilder withdrawScenario = scenario("Withdraw Scenario")
            .exec(login)
            .exec(withdraw);

    ScenarioBuilder transferScenario = scenario("Transfer Scenario")
            .exec(login)
            .exec(transfer);

    {
        setUp(
                depositScenario.injectOpen(rampUsers(500).during(60)),
                withdrawScenario.injectOpen(rampUsers(500).during(60)),
                transferScenario.injectOpen(rampUsers(200).during(60))
        ).protocols(httpProtocol);
    }
}
