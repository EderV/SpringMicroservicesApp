package com.evm.ms.gateway.domain.exceptions;

public class ClientForbiddenException extends RuntimeException {

    public ClientForbiddenException(String msg) {
        super(msg);
    }

}
