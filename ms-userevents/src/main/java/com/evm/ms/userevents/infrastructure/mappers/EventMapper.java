package com.evm.ms.userevents.infrastructure.mappers;

import com.evm.ms.userevents.domain.Event;
import com.evm.ms.userevents.infrastructure.dto.entity.EventEntity;
import com.evm.ms.userevents.infrastructure.dto.request.EventRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.ZonedDateTime;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventMapper MAPPER = Mappers.getMapper(EventMapper.class);

    Event toEvent(EventRequest eventRequest);

    @Mapping(
            target = "eventDateTime",
            expression = "java(stringToZonedDateTime(eventEntity.getEventDateTime()))"
    )
    Event toEvent(EventEntity eventEntity);


    @Mapping(
            target = "eventDateTime",
            expression = "java(event.getEventDateTime().toString())"
    )
    EventEntity toEventEntity(Event event);

    @Named("stringToZonedDateTime")
    default ZonedDateTime stringToZonedDateTime(String dateTime) {
        return ZonedDateTime.parse(dateTime);
    }

}
