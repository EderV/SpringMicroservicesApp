package com.evm.ms.auth.domain.in;

import com.evm.ms.auth.domain.Credentials;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface AuthServicePort {

    Authentication login(Credentials credentials) throws AuthenticationException;

}
