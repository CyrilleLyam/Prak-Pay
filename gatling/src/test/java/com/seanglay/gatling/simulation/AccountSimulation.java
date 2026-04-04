package com.seanglay.gatling.simulation;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.UUID;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class AccountSimulation extends Simulation {

    private static final String BASE_URL = System.getProperty("baseUrl", "http://localhost:8080");

    HttpProtocolBuilder httpProtocol = http.baseUrl(BASE_URL).acceptHeader("application/json").contentTypeHeader("application/json");

    ChainBuilder register = exec(http("Register Account").post("/api/v1/accounts/register").body(StringBody(session -> """
            {
              "name": "Test User",
              "email": "user-%s@test.com",
              "password": "Test@1234"
            }
            """.formatted(UUID.randomUUID()))).check(status().is(201)));

    ChainBuilder login = exec(http("Login Account").post("/api/v1/accounts/login").body(StringBody("""
            {
              "email": "seed@test.com",
              "password": "Test@1234"
            }
            """)).check(status().is(200)).check(jsonPath("$.data.access_token").saveAs("token")));


    ScenarioBuilder registerScenario = scenario("Register Scenario").exec(register);

    ScenarioBuilder loginScenario = scenario("Login Scenario").exec(login);

    {
        setUp(
            registerScenario.injectOpen(rampUsers(10000).during(60)),
            loginScenario.injectOpen(rampUsers(10000).during(60))
        ).protocols(httpProtocol);
    }
}
