package com.evm.ms.auth.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

    private UUID id;

    private User user;

    private String role;

    private Boolean enabled;

    private Date createdAt;

    @Override
    public String getAuthority() {
        return role;
    }

}
