package com.evm.ms.auth.domain.component;

import java.security.Key;

public interface KeyUtils {

    Key getAccessTokenPrivateKey();

    Key getAccessTokenPublicKey();

}
