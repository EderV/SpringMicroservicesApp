package com.evm.ms.auth.infrastructure.mappers;

import com.evm.ms.auth.domain.Credentials;
import com.evm.ms.auth.infrastructure.dto.request.CredentialsRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

    CredentialsMapper MAPPER = Mappers.getMapper(CredentialsMapper.class);

    Credentials toCredentials(CredentialsRequest credentialsRequest);

}
