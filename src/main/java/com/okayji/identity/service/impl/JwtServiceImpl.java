package com.okayji.identity.service.impl;

import com.okayji.identity.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
@Slf4j(topic = "JWT-SERVICE")
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.signerKey}")
    private String signerKey;
    @Value("${jwt.accessTokenTime}")
    private int accessTokenTime;
    @Value("${jwt.accessTokenTimeWithRemember}")
    private int accessTokenTimeWithRemember;

    @Override
    public String generateAccessToken(String userId,
                                      String username,
                                      List<String> authorities,
                                      boolean rememberMe) {
        log.info("Generate access token for user {} with authorities {}", username, authorities);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", authorities);
        claims.put("userId", userId);

        long liveTime = (rememberMe
                ? accessTokenTimeWithRemember
                : accessTokenTime) * 1000L * 60 * 60 * 24;

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + liveTime))
                .id(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, getSignerKey())
                .compact();
    }

    @Override
    public Jws<Claims> getClaimsJws(String token) {
        return Jwts.parser()
                .setSigningKey(getSignerKey())
                .build()
                .parseClaimsJws(token);
    }

    private SecretKey getSignerKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(signerKey));
    }
}
