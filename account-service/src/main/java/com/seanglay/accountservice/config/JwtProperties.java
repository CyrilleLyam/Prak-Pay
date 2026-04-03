package com.seanglay.accountservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.security.jwt")
public class JwtProperties {
    private long accessTokenExpiration;
    private String privateKey;
    private String publicKey;
}
