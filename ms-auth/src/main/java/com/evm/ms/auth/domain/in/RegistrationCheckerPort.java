package com.evm.ms.auth.domain.in;

import com.evm.ms.auth.domain.Registration;

public interface RegistrationCheckerPort {

    void checkRegistration(Registration registration) throws IllegalArgumentException;

}
