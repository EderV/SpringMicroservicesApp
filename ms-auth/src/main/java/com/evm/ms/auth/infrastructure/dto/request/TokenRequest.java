package com.evm.ms.auth.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequest {

    private UUID userId;
    private String accessToken;
    private int sessionExpiration;

}
