package com.evm.ms.auth.domain.out;

import com.evm.ms.auth.domain.User;

public interface UserRepositoryPort {

    void saveUser(User user);

    User getUserFromUsername(String username);

    User getUserFromEmail(String email);

}
