package com.evm.ms.auth.infrastructure.security;

import com.evm.ms.auth.domain.exceptions.CustomAuthenticationException;
import com.evm.ms.auth.domain.in.JwtServicePort;
import com.evm.ms.auth.infrastructure.factory.RequestMatcherFactory;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${private_endpoints}")
    private List<String> privateEndpoints;

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JwtServicePort jwtServicePort;
    private final UserDetailsService userDetailsService;
    private final RequestMatcherFactory requestMatcherFactory;
    private final List<RequestMatcher> privateRequestMatchers = new ArrayList<>();

    @PostConstruct
    private void postConstruct() {
        for (var privateEndpoint : privateEndpoints) {
            privateRequestMatchers.add(requestMatcherFactory.getRequestMatcher(privateEndpoint));
        }
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        boolean urlMatchesMatcher = privateRequestMatchers.stream().anyMatch(matcher -> matcher.matches(request));

        if (urlMatchesMatcher) {
            try {
                var jwt = extractJwtFromHeader(request);

                if (isJwtValid(jwt)) {
                    var user = loadUserFromRepository(jwt);
                    setAuthenticationInSecurityContext(user, request);
                }
            } catch (AuthenticationException ex) {
                authenticationEntryPoint.commence(request, response, ex);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromHeader(HttpServletRequest request) throws AuthenticationException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new CustomAuthenticationException("Incorrect authorization header");
    }

    private boolean isJwtValid(String jwt) throws AuthenticationException {
        try {
            return jwtServicePort.validateAccessToken(jwt);
        } catch (SignatureException e) {
            throw new CustomAuthenticationException("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            throw new CustomAuthenticationException("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new CustomAuthenticationException("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new CustomAuthenticationException("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new CustomAuthenticationException("JWT claims string is empty: " + e.getMessage());
        }
    }

    private UserDetails loadUserFromRepository(String jwt) throws AuthenticationException {
        try {
            var username = jwtServicePort.extractUsernameFromAccessToken(jwt);
            return userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new CustomAuthenticationException("Username extracted from JWT not found in the database");
        }
    }

    private void setAuthenticationInSecurityContext(UserDetails userDetails, HttpServletRequest request) {
        var authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
