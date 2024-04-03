package com.evm.ms.auth.application;

import com.evm.ms.auth.domain.Registration;
import com.evm.ms.auth.domain.Role;
import com.evm.ms.auth.domain.User;
import com.evm.ms.auth.domain.exceptions.UserAlreadyRegisteredException;
import com.evm.ms.auth.domain.in.RegistrationServicePort;
import com.evm.ms.auth.domain.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RegistrationServiceDefault implements RegistrationServicePort {

    private final PasswordEncoder passwordEncoder;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void registerUser(Registration registration) throws UserAlreadyRegisteredException {
        var email = registration.getEmail();
        var username = registration.getUsername();
        var password = registration.getPassword();

        var userByEmail = userRepositoryPort.getUserFromEmail(email);
        if (userByEmail != null) {
            throw new UserAlreadyRegisteredException("Email " + email + " already exists");
        }

        var userByUsername = userRepositoryPort.getUserFromUsername(username);
        if (userByUsername != null) {
            throw new UserAlreadyRegisteredException("Username " + username + " already exists");
        }

        var user = createUserEntity(email, username, password);
        userRepositoryPort.saveUser(user);
    }

    private User createUserEntity(String email, String username, String password) {
        var user = User.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .accountEnabled(true)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();

        var roleDefault = Role.builder()
                .user(user)
                .role("ROLE_USER")
                .enabled(true)
                .build();

        var roles = Arrays.asList(roleDefault); // , roleUser);
        user.setRoles(roles);

        return user;
    }

}
