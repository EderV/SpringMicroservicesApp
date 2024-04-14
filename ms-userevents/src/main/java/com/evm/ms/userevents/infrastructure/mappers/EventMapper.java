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
            expression = "java(zonedDateTimeToString(event.getEventDateTime()))"
    )
    EventEntity toEventEntity(Event event);

    EventRequest toEventRequest(Event event);

    @Named("stringToZonedDateTime")
    default ZonedDateTime stringToZonedDateTime(String dateTime) {
        if (dateTime == null || dateTime.isBlank()) return null;

        return ZonedDateTime.parse(dateTime);
    }

    @Named("zonedDateTimeToString")
    default String zonedDateTimeToString(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) return null;

        return zonedDateTime.toString();
    }

}
