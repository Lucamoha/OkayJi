package com.okayji.identity.service.impl;

import com.okayji.identity.dto.request.AuthenticationRequest;
import com.okayji.identity.dto.response.AuthenticationResponse;
import com.okayji.identity.entity.InvalidatedToken;
import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.identity.entity.User;
import com.okayji.identity.repository.InvalidatedTokenRepository;
import com.okayji.identity.service.AuthenticationService;
import com.okayji.identity.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {

    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        log.info("Authentication Request: username={}", authenticationRequest.getUsername());

        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(), authenticationRequest.getPassword()
                    ));
        } catch (InternalAuthenticationServiceException exception) {
            throw new AppException(AppError.USER_NOT_FOUND);
        } catch (BadCredentialsException exception) {
            throw new AppException(AppError.WRONG_PASSWORD);
        }

        log.info("isAuthenticated = {}", authentication.isAuthenticated());
        log.info("Authorities: {}", authentication.getAuthorities().toString());

        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(authority -> authority.startsWith("ROLE_")).toList();
        User user = (User) authentication.getPrincipal();

        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateAccessToken(
                        user.getId(),
                        user.getUsername(),
                        authorities,
                        authenticationRequest.isRememberMe()))
                .build();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authHeader.substring(7);

        Claims claims = jwtService.getClaimsJws(token).getBody();
        String jwtId = claims.getId();
        Date exp = claims.getExpiration();

        InvalidatedToken invalidatedToken = new InvalidatedToken(jwtId, exp);
        invalidatedTokenRepository.save(invalidatedToken);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logoutHandler.logout(request, response, authentication);
    }
}
