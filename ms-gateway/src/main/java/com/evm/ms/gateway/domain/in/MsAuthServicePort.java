package com.evm.ms.gateway.domain.in;

public interface MsAuthServicePort {

    int isClientAllowed(String authorizationHeader, String path);

}
