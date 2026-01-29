package com.okayji.identity.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.List;

public interface JwtService {
    String generateAccessToken(String username, List<String> authorities, boolean rememberMe);
    Jws<Claims> getClaimsJws(String token);
}
