package com.evm.ms.auth.infrastructure.mappers;

import com.evm.ms.auth.domain.Token;
import com.evm.ms.auth.infrastructure.dto.request.TokenRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TokenMapper {

    TokenMapper MAPPER = Mappers.getMapper(TokenMapper.class);

    TokenRequest toTokenRequest(Token token);

}
