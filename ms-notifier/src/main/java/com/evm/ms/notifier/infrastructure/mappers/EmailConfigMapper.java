package com.evm.ms.notifier.infrastructure.mappers;

import com.evm.ms.notifier.domain.config.EmailConfig;
import com.evm.ms.notifier.infrastructure.dto.entity.EmailConfigEntity;
import com.evm.ms.notifier.infrastructure.dto.request.EmailConfigRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmailConfigMapper {

    EmailConfigMapper MAPPER = Mappers.getMapper(EmailConfigMapper.class);

    EmailConfig toEmailConfig(EmailConfigRequest emailConfigRequest);

    EmailConfig toEmailConfig(EmailConfigEntity emailConfigEntity);

    EmailConfigRequest toEmailConfigRequest(EmailConfig emailConfig);

    EmailConfigEntity toEmailConfigEntity(EmailConfig emailConfig);

}
