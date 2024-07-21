package com.evm.ms.userevents.infrastructure.adapters;

import com.evm.ms.userevents.domain.Event;
import com.evm.ms.userevents.domain.ports.out.EventRepositoryPort;
import com.evm.ms.userevents.infrastructure.dto.entity.EventEntity;
import com.evm.ms.userevents.infrastructure.mappers.EventMapper;
import com.evm.ms.userevents.infrastructure.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.evm.ms.userevents.infrastructure.utils.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventRepositoryAdapter implements EventRepositoryPort {

    private final BeanUtils beanUtils;
    private final EventRepository eventRepository;

    @Override
    public List<Event> findAll() {
        var eventEntities = eventRepository.findAll();
        return toEvent(eventEntities);
    }

    @Override
    public List<Event> findAllByUserId(String userId) {
        var eventEntities = eventRepository.findAllByUserId(userId);
        return toEvent(eventEntities);
    }

    @Override
    public Event findById(String id) {
        var event = eventRepository.findById(id);
        return event.map(this::toEvent).orElse(null);
    }

    @Override
    public Event saveEvent(Event event) {
        log.error("ZonedDateTime: " + event.getEventDateTime().toString() + ", TimeZone: " + event.getEventDateTime().getZone());
        var eventEntity = toEventEntity(event);
        log.error("ZonedDateTime: " + eventEntity.getEventDateTime());

        var savedEvent = eventRepository.insert(eventEntity);
        return toEvent(savedEvent);
    }

    @Override
    public Event updateEventOnlyNonNulls(Event event) {
        var eventEntity = toEventEntity(event);
        var id = eventEntity.getId();
        if (id == null) return null;

        var oldEventEntity = eventRepository.findById(id);
        if (oldEventEntity.isEmpty()) return null;

        var modifiedEventEntity = oldEventEntity.get();
        beanUtils.copyPropertiesIgnoringNulls(eventEntity, modifiedEventEntity);

        var updatedEvent = eventRepository.save(modifiedEventEntity);
        return toEvent(updatedEvent);
    }

    @Override
    public void updateEventTriggered(String id, boolean triggered) {
        eventRepository.updateTriggeredById(id, triggered);
    }

    private EventEntity toEventEntity(Event event) {
        return EventMapper.MAPPER.toEventEntity(event);
    }

    private Event toEvent(EventEntity eventEntity) {
        return EventMapper.MAPPER.toEvent(eventEntity);
    }

    private List<Event> toEvent(List<EventEntity> eventEntities) {
        return eventEntities.stream().map((EventMapper.MAPPER::toEvent)).collect(Collectors.toList());
    }

}
