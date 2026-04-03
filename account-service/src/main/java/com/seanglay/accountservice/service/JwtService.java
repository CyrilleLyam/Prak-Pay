package com.seanglay.accountservice.service;

import com.seanglay.accountservice.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public String generateAccessToken(UUID accountId, String email) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(jwtProperties.getAccessTokenExpiration());

        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("account-service").issuedAt(now).expiresAt(expiry).subject(accountId.toString()).claim("email", email).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(SignatureAlgorithm.RS512).build(), claims)).getTokenValue();
    }

    public Instant getAccessTokenExpiry() {
        return Instant.now().plusSeconds(jwtProperties.getAccessTokenExpiration());
    }
}
