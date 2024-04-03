package com.evm.ms.auth.infrastructure.controller;

import com.evm.ms.auth.domain.Credentials;
import com.evm.ms.auth.domain.Registration;
import com.evm.ms.auth.domain.Token;
import com.evm.ms.auth.domain.in.*;
import com.evm.ms.auth.infrastructure.dto.request.CredentialsRequest;
import com.evm.ms.auth.infrastructure.dto.request.RegistrationRequest;
import com.evm.ms.auth.infrastructure.dto.request.TokenRequest;
import com.evm.ms.auth.infrastructure.mappers.CredentialsMapper;
import com.evm.ms.auth.infrastructure.mappers.RegistrationMapper;
import com.evm.ms.auth.infrastructure.mappers.TokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final RegistrationCheckerPort registrationCheckerPort;
    private final RegistrationServicePort registrationServicePort;
    private final AuthCheckerPort authCheckerPort;
    private final AuthServicePort authServicePort;
    private final JwtServicePort jwtServicePort;

    @Value("${access-token.duration_minutes}")
    private int accessTokenDuration;

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test OK");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredentialsRequest credentialsRequest)
            throws IllegalArgumentException, AuthenticationException {

        var credentials = toCredentials(credentialsRequest);

        authCheckerPort.checkCredentials(credentials);

        var authentication = authServicePort.login(credentials);
        var tokenRequest = generateTokenRequest(authentication);

        return new ResponseEntity<>(tokenRequest, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest)
            throws IllegalArgumentException {

        var registration = toRegistration(registrationRequest);

        registrationCheckerPort.checkRegistration(registration);

        registrationServicePort.registerUser(registration);

        return new ResponseEntity<>("User registered in DB", HttpStatus.CREATED);
    }

    private TokenRequest generateTokenRequest(Authentication authentication) {
        var token = jwtServicePort.getAccessToken(authentication);
        var tokenRequest = toTokenRequest(token);

        tokenRequest.setSessionExpiration(accessTokenDuration);

        return tokenRequest;
    }

    private Credentials toCredentials(CredentialsRequest credentialsRequest) {
        return CredentialsMapper.MAPPER.toCredentials(credentialsRequest);
    }

    private TokenRequest toTokenRequest(Token token) {
        return TokenMapper.MAPPER.toTokenRequest(token);
    }

    private Registration toRegistration(RegistrationRequest registrationRequest) {
        return RegistrationMapper.MAPPER.toRegistration(registrationRequest);
    }

}
