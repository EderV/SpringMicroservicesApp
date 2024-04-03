package com.evm.ms.auth.infrastructure.mappers;

import com.evm.ms.auth.domain.Registration;
import com.evm.ms.auth.infrastructure.dto.request.RegistrationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {

    RegistrationMapper MAPPER = Mappers.getMapper(RegistrationMapper.class);

    Registration toRegistration(RegistrationRequest registrationRequest);

}
