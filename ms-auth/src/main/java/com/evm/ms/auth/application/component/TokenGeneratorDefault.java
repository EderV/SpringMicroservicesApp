package com.evm.ms.auth.application.component;

import com.evm.ms.auth.domain.Token;
import com.evm.ms.auth.domain.User;
import com.evm.ms.auth.domain.component.KeyUtils;
import com.evm.ms.auth.domain.component.TokenGenerator;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class TokenGeneratorDefault implements TokenGenerator {

    @Value("${access-token.duration_minutes}")
    private int accessTokenDuration;

    private final KeyUtils keyUtils;

    @Override
    public Token createToken(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof User user)) {
            throw new BadCredentialsException(
                    MessageFormat.format("Principal {0} is not of User type", authentication.getPrincipal())
            );
        }

        Token token = new Token();
        token.setUserId(user.getId());
        token.setAccessToken(createAccessToken(authentication));

        return token;
    }
//
    private String createAccessToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();
        Instant expirationDate = now.plus(accessTokenDuration, ChronoUnit.MINUTES);

        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expirationDate))
                .signWith(keyUtils.getAccessTokenPrivateKey())
                .compact();
    }

}
