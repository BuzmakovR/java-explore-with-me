package ru.practicum.explorewithme.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewStats {

	private String app;

	private String uri;

	private Long hits;
}