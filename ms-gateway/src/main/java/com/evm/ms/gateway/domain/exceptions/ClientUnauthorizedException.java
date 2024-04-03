package com.evm.ms.gateway.domain.exceptions;

public class ClientUnauthorizedException extends RuntimeException {

    public ClientUnauthorizedException(String msg) {
        super(msg);
    }

}
