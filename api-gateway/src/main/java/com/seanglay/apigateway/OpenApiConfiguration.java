package com.seanglay.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class OpenApiConfiguration {

    @Value("${app.gateway-url}")
    private String gatewayUrl;

    @Bean
    public RewriteFunction<String, String> openApiRewriteFunction() {
        return (exchange, body) -> {
            String modifiedBody = body.replaceAll("\"url\"\\s*:\\s*\"https?://[^\"]+\"", "\"url\":\"" + gatewayUrl + "\"");
            return Mono.just(modifiedBody);
        };
    }
}
