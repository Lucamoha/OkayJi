package com.okayji.config;

import com.okayji.common.ApiResponse;
import com.okayji.exception.AppError;
import com.okayji.identity.entity.User;
import com.okayji.identity.repository.InvalidatedTokenRepository;
import com.okayji.identity.repository.UserRepository;
import com.okayji.identity.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "JWT-AUTHENTICATION-FILTER")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.equals("/swagger-ui.html");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasLength(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Claims claims = jwtService.getClaimsJws(token).getBody();
                String jwtId = claims.getId();

                if (invalidatedTokenRepository.existsById(jwtId))
                    throw new JwtException("Invalid token");

                String userId =  claims.getSubject();
                log.info("userId: {} - {} {}", userId, request.getMethod(), request.getRequestURI());

                User user = userRepository.findUserById(userId);

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            } catch (JwtException ex) {
                log.info(ex.getMessage());

                AppError appError = AppError.UNAUTHENTICATED;

                response.setStatus(appError.getHttpStatusCode().value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                ApiResponse<?> apiResponse = ApiResponse.builder()
                        .message(appError.getMessage())
                        .build();

                ObjectMapper objectMapper = new ObjectMapper();

                response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
