package ru.practicum.explorewithme.dto.event.fitler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dto.event.state.EventStates;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventAdminSearchFilters {

	private List<Long> users;

	private List<EventStates> states;

	private List<Long> categories;

	private LocalDateTime rangeStart;

	private LocalDateTime rangeEnd;

	private Integer from;

	private Integer size;
}
