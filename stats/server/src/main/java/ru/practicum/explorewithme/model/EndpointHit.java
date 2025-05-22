package ru.practicum.explorewithme.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "endpoint_hits")
public class EndpointHit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "app", nullable = false)
	private String app;

	@Column(name = "uri", nullable = false)
	private String uri;

	@Column(name = "ip", nullable = false)
	private String ip;

	@Column(name = "timestamp", nullable = false)
	private LocalDateTime timestamp;
}
