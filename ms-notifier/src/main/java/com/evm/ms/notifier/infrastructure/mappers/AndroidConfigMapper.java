package com.evm.ms.notifier.infrastructure.mappers;

import com.evm.ms.notifier.domain.config.AndroidConfig;
import com.evm.ms.notifier.infrastructure.dto.entity.AndroidConfigEntity;
import com.evm.ms.notifier.infrastructure.dto.request.AndroidConfigRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AndroidConfigMapper {

    AndroidConfigMapper MAPPER = Mappers.getMapper(AndroidConfigMapper.class);

    AndroidConfig toAndroidConfig(AndroidConfigRequest androidConfigRequest);

    AndroidConfig toAndroidConfig(AndroidConfigEntity androidConfigEntity);

    AndroidConfigRequest toAndroidConfigRequest(AndroidConfig androidConfig);

    AndroidConfigEntity toAndroidConfigEntity(AndroidConfig androidConfig);

}
