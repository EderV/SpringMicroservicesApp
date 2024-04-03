package com.evm.ms.auth.infrastructure.mappers;

import com.evm.ms.auth.domain.Role;
import com.evm.ms.auth.domain.User;
import com.evm.ms.auth.infrastructure.dto.entity.RoleEntity;
import com.evm.ms.auth.infrastructure.dto.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "roles", expression = "java(mapRoles(userEntity))")
    User toUser(UserEntity userEntity);

//    @Mapping(target = "roles", expression = "java(mapRoleEntities(user))")
//    UserEntity toUserEntity(User user);

    default List<Role> mapRoles(UserEntity userEntity) {
        var userWithId = User.builder().id(userEntity.getId()).build();
        var roles = new ArrayList<Role>();
        for (var roleEntity : userEntity.getRoles()) {
            var role = new Role();
            role.setUser(userWithId);
            role.setRole(roleEntity.getRole());
            role.setEnabled(roleEntity.getEnabled());
            roles.add(role);
        }
        return roles;
    }

    default UserEntity toUserEntity(User user) {
        var userEntity = UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .accountEnabled(user.getAccountEnabled())
                .accountExpired(user.getAccountExpired())
                .accountLocked(user.getAccountLocked())
                .credentialsExpired(user.getCredentialsExpired())
                .build();

        var roleEntities = new ArrayList<RoleEntity>();
        for (var role : user.getRoles()) {
            var roleEntity = new RoleEntity();
            roleEntity.setUser(userEntity);
            roleEntity.setRole(role.getRole());
            roleEntity.setEnabled(role.getEnabled());
            roleEntities.add(roleEntity);
        }

        userEntity.setRoles(roleEntities);
        return userEntity;
    }

}
