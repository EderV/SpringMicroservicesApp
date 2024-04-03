package com.evm.ms.auth.infrastructure.dto.request;

import lombok.Data;

@Data
public class CredentialsRequest {

    String username;
    String password;

}
