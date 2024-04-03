package com.evm.ms.auth.application;

import com.evm.ms.auth.application.base.BaseTextChecker;
import com.evm.ms.auth.domain.Credentials;
import com.evm.ms.auth.domain.in.AuthCheckerPort;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AuthCheckerDefault extends BaseTextChecker implements AuthCheckerPort {

    @Override
    public void checkCredentials(Credentials credentials) throws IllegalArgumentException {
        nullCheck(credentials);
        checkMinimumDataRequired(credentials);
        checkIllegalCharactersInAllFields(credentials);
    }

    private void checkMinimumDataRequired(Credentials credentials) throws IllegalArgumentException {
        if (credentials.getUsername() == null || credentials.getUsername().isBlank() ||
            credentials.getPassword() == null || credentials.getPassword().isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Data provided in Credentials is not minimum required and/or is not correct");
        }
    }

    private void checkIllegalCharactersInAllFields(Credentials credentials) throws IllegalArgumentException {
        var username = credentials.getUsername();
        var password = credentials.getPassword();

        var texts = Arrays.asList(username, password);
        checkIllegalCharacters(texts);
    }

}
