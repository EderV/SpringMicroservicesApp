package com.evm.ms.auth.domain.in;

import com.evm.ms.auth.domain.Registration;
import com.evm.ms.auth.domain.exceptions.UserAlreadyRegisteredException;

public interface RegistrationServicePort {

    void registerUser(Registration registration) throws UserAlreadyRegisteredException;

}
