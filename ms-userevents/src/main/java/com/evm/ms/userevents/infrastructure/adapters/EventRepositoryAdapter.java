package com.evm.ms.userevents.infrastructure.adapters;

import com.evm.ms.userevents.domain.Event;
import com.evm.ms.userevents.domain.ports.out.EventRepositoryPort;
import com.evm.ms.userevents.infrastructure.dto.entity.EventEntity;
import com.evm.ms.userevents.infrastructure.mappers.EventMapper;
import com.evm.ms.userevents.infrastructure.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventRepositoryAdapter implements EventRepositoryPort {

    private final EventRepository eventRepository;

    @Override
    public void saveEvent(Event event) {
        log.error("ZonedDateTime: " + event.getEventDateTime().toString() + ", TimeZone: " + event.getEventDateTime().getZone());
        var eventEntity = toEventEntity(event);
        log.error("ZonedDateTime: " + eventEntity.getEventDateTime());
        eventRepository.insert(eventEntity);

        log.error("AAAA: " + EventMapper.MAPPER.toEvent(eventEntity).getEventDateTime());
    }

    private EventEntity toEventEntity(Event event) {
        return EventMapper.MAPPER.toEventEntity(event);
    }

}
