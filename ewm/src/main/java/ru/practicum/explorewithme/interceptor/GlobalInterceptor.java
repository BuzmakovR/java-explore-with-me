package ru.practicum.explorewithme.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.practicum.explorewithme.client.StatsClient;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.utils.DateTimeUtils;

import java.time.LocalDateTime;

@Slf4j
@Setter
@Getter
@Component
public class GlobalInterceptor implements HandlerInterceptor {

	@Value("${stats-server.name}")
	private String appName;

	private final StatsClient statsClient;

	@Autowired
	public GlobalInterceptor(StatsClient statsClient) {
		this.statsClient = statsClient;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			ResponseEntity<Object> statsResponse = statsClient.addHit(EndpointHitDto.builder()
					.app(appName)
					.uri(request.getRequestURI())
					.ip(request.getRemoteAddr())
					.timestamp(DateTimeUtils.dateTimeToString(LocalDateTime.now()))
					.build());
			if (!statsResponse.getStatusCode().is2xxSuccessful()) {
				log.error("Ошибка при сохранении статистики: {}", statsResponse.getBody());
			}
		} catch (RuntimeException e) {
			log.error("Произошла непредвиденная ошибка: {}", e.getMessage());
		}
		return true;
	}
}