package ru.practicum.explorewithme.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dto.event.state.EventStates;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User initiator;

	@Column(nullable = false)
	private String annotation;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@Column(name = "event_date", nullable = false)
	private LocalDateTime eventDate;

	@ManyToOne
	private Category category;

	@Column(nullable = false)
	private Double lat;

	@Column(nullable = false)
	private Double lon;

	@Column(name = "confirmed_requests", nullable = false)
	private Long confirmedRequests = 0L;

	@Column(nullable = false)
	private Boolean paid = false;

	@Column(name = "participant_limit", nullable = false)
	private Long participantLimit = 0L;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private EventStates state;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Column(name = "published_on")
	private LocalDateTime publishedOn;

	@Column(name = "request_moderation", nullable = false)
	private Boolean requestModeration = true;

	@Column
	private Long views;
}
