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
	Long id;

	@ManyToOne
	User initiator;

	@Column(nullable = false)
	String annotation;

	@Column(nullable = false)
	String title;

	@Column(nullable = false)
	String description;

	@Column(name = "event_date", nullable = false)
	LocalDateTime eventDate;

	@ManyToOne
	Category category;

	@Column(nullable = false)
	Double lat;

	@Column(nullable = false)
	Double lon;

	@Column(name = "confirmed_requests", nullable = false)
	Long confirmedRequests = 0L;

	@Column(nullable = false)
	Boolean paid = false;

	@Column(name = "participant_limit", nullable = false)
	Long participantLimit = 0L;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	EventStates state;

	@Column(name = "created_on", nullable = false)
	LocalDateTime createdOn;

	@Column(name = "published_on")
	LocalDateTime publishedOn;

	@Column(name = "request_moderation", nullable = false)
	Boolean requestModeration = true;

	@Column
	Long views;
}
