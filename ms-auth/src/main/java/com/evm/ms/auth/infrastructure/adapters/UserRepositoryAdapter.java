package com.evm.ms.auth.infrastructure.adapters;

import com.evm.ms.auth.domain.User;
import com.evm.ms.auth.domain.out.UserRepositoryPort;
import com.evm.ms.auth.infrastructure.dto.entity.UserEntity;
import com.evm.ms.auth.infrastructure.mappers.UserMapper;
import com.evm.ms.auth.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;

    @Override
    public void saveUser(User user) {
        var userEntity = toUserEntity(user);
        userRepository.save(userEntity);
    }

    @Override
    public User getUserFromUsername(String username) {
        var userEntity = userRepository.getUserByUsername(username);
        return userEntity.map(this::toUser).orElse(null);
    }

    @Override
    public User getUserFromEmail(String email) {
        var userEntity = userRepository.getUserByEmail(email);
        return userEntity.map(this::toUser).orElse(null);
    }

    private User toUser(UserEntity userEntity) {
        return UserMapper.MAPPER.toUser(userEntity);
    }

    private UserEntity toUserEntity(User user) {
        return UserMapper.MAPPER.toUserEntity(user);
    }

}
