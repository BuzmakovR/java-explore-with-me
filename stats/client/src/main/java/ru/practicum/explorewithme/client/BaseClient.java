package ru.practicum.explorewithme.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BaseClient {

	protected final RestTemplate rest;

	protected ResponseEntity<Object> get(String path, @Nullable Map<String, Object> parameters) {
		return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
	}

	protected <T> ResponseEntity<Object> post(String path, T body) {
		return makeAndSendRequest(HttpMethod.POST, path, null, body);
	}

	private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, @Nullable Map<String, Object> parameters, @Nullable T body) {
		HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

		ResponseEntity<Object> serverResponse;
		try {
			if (parameters != null) {
				serverResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
			} else {
				serverResponse = rest.exchange(path, method, requestEntity, Object.class);
			}
		} catch (HttpStatusCodeException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
		}
		return prepareGatewayResponse(serverResponse);
	}

	private HttpHeaders defaultHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		return headers;
	}

	private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
		if (response.getStatusCode().is2xxSuccessful()) {
			return response;
		}

		ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
		if (response.hasBody()) {
			return responseBuilder.body(response.getBody());
		}
		return responseBuilder.build();
	}
}
