package com.evm.ms.auth.domain.in;

import com.evm.ms.auth.domain.Credentials;

public interface AuthCheckerPort {

    void checkCredentials(Credentials credentials) throws IllegalArgumentException;

}
