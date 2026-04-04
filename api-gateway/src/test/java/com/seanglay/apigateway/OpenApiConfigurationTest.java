package com.seanglay.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.web.server.ServerWebExchange;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;

@SpringBootTest
class OpenApiConfigurationTest {

    @Autowired
    private RewriteFunction<String, String> openApiRewriteFunction;

    @Test
    void shouldRewriteOpenApiUrl() {
        String originalBody = "{\"openapi\":\"3.0.1\",\"info\":{\"title\":\"Account Service API\",\"version\":\"v1\"},\"servers\":[{\"url\":\"http://localhost:8084\",\"description\":\"Generated server url\"}],\"paths\":{}}";
        String expectedBody = "{\"openapi\":\"3.0.1\",\"info\":{\"title\":\"Account Service API\",\"version\":\"v1\"},\"servers\":[{\"url\":\"http://localhost:8080\",\"description\":\"Generated server url\"}],\"paths\":{}}";

        ServerWebExchange exchange = mock(ServerWebExchange.class);

        Publisher<String> result = openApiRewriteFunction.apply(exchange, originalBody);

        StepVerifier.create(result)
                .expectNext(expectedBody)
                .verifyComplete();
    }

    @Test
    void shouldHandleVariousHostnames() {
        String originalBody = "{\"servers\":[{\"url\":\"http://host.docker.internal:8084\"},{\"url\":\"https://account-service.default.svc.cluster.local:443\"}]}";
        String expectedBody = "{\"servers\":[{\"url\":\"http://localhost:8080\"},{\"url\":\"http://localhost:8080\"}]}";

        ServerWebExchange exchange = mock(ServerWebExchange.class);

        Publisher<String> result = openApiRewriteFunction.apply(exchange, originalBody);

        StepVerifier.create(result)
                .expectNext(expectedBody)
                .verifyComplete();
    }
}
