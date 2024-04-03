package com.evm.ms.auth.domain.component;

import com.evm.ms.auth.domain.Token;
import org.springframework.security.core.Authentication;

public interface TokenGenerator {

    Token createToken(Authentication authentication);

}
