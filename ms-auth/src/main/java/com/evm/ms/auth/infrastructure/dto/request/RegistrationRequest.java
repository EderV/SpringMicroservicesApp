package com.evm.ms.auth.infrastructure.dto.request;

import lombok.Data;

@Data
public class RegistrationRequest {

    private String email;
    private String username;
    private String password;

}
