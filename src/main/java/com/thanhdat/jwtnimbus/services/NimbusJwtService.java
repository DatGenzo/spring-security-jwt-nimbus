package com.thanhdat.jwtnimbus.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.thanhdat.jwtnimbus.entities.User;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NimbusJwtService {
    private final byte[] secret;
    private final String issuer;
    private final long expirationTime;

    public NimbusJwtService(
        @Value("${security.jwt.secret}") String secret,
        @Value("${security.jwt.issuer}") String issuer,
        @Value("${security.jwt.expiration-time}") long expirationTime
    ) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.issuer = issuer;
        this.expirationTime = expirationTime;
        if (this.secret.length < 32) throw new IllegalArgumentException("JWT secret phải có ít nhất 32 byte");
    }

    public String generateToken(User user) {
        Instant now = Instant.now();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
            .issuer(issuer)
            .subject(user.getEmail())
            .claim("userId", user.getId())
            .claim("fullName", user.getFullName())
            .claim("role", "ROLE_" + user.getRole().name())
            .issueTime(Date.from(now))
            .notBeforeTime(Date.from(now))
            .expirationTime(Date.from(now.plusMillis(expirationTime)))
            .build();
        SignedJWT jwt = new SignedJWT(
            new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build(),
            claims
        );
        try {
            jwt.sign(new MACSigner(secret));
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new IllegalStateException("Không thể tạo Nimbus JWT", e);
        }
    }

    public JWTClaimsSet parseAndValidate(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            if (!JWSAlgorithm.HS256.equals(jwt.getHeader().getAlgorithm()))
                throw new IllegalArgumentException("Thuật toán JWT không hợp lệ");
            if (!jwt.verify(new MACVerifier(secret)))
                throw new IllegalArgumentException("Chữ ký JWT không hợp lệ");
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            Date now = new Date();
            if (!issuer.equals(claims.getIssuer())) throw new IllegalArgumentException("Issuer không hợp lệ");
            if (claims.getExpirationTime() == null || claims.getExpirationTime().before(now))
                throw new IllegalArgumentException("JWT đã hết hạn");
            if (claims.getNotBeforeTime() != null && claims.getNotBeforeTime().after(now))
                throw new IllegalArgumentException("JWT chưa có hiệu lực");
            if (claims.getSubject() == null || claims.getSubject().isBlank())
                throw new IllegalArgumentException("JWT không có subject");
            return claims;
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException iae) throw iae;
            throw new IllegalArgumentException("JWT không hợp lệ", e);
        }
    }

    public String extractUsername(String token) { return parseAndValidate(token).getSubject(); }
    public boolean isTokenValid(String token, User user) {
        return user.getEmail().equalsIgnoreCase(extractUsername(token)) && user.isEnabled() && user.isAccountNonLocked();
    }
    public long getExpirationTime() { return expirationTime; }
}
