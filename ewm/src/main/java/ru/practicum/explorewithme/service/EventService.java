package ru.practicum.explorewithme.service;

import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.event.UpdateEventAdminRequest;
import ru.practicum.explorewithme.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.dto.event.fitler.EventAdminSearchFilters;
import ru.practicum.explorewithme.dto.event.fitler.EventSearchFilters;
import java.util.List;

@Service
public interface EventService {

	EventFullDto getPublished(Long eventId);

	List<EventShortDto> get(Long userId, Integer from, Integer size);

	List<EventFullDto> get(EventAdminSearchFilters eventAdminSearchFilters);

	List<EventShortDto> get(EventSearchFilters eventSearchFilters);

	EventFullDto getById(Long userId, Long id);

	EventFullDto add(Long userId, NewEventDto newEventDto);

	EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

	EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

}
