package ru.practicum.explorewithme.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStatsDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {

	@Autowired
	public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
		super(
				builder
						.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
						.requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
						.build()
		);
	}

	public ResponseEntity<Object> addHit(EndpointHitDto endpointHitDto) {
		return post("/hit", endpointHitDto);
	}

	public List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
		ResponseEntity<Object> responseEntity = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
				Map.of("start", start,
						"end", end,
						"uris", String.join(",", uris),
						"unique", unique)
		);
		if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.convertValue(responseEntity.getBody(),
					new TypeReference<List<ViewStatsDto>>() {});
		}
		return Collections.emptyList();
	}
}
