package ru.practicum.explorewithme.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.practicum.explorewithme.utils.DateTimeUtils;

import java.time.LocalDateTime;

@Getter
public class ApiError {

	private final String status;

	private final String reason;

	private final String message;

	private final String timestamp;

	public ApiError(HttpStatus httpStatus, String reason, String message) {
		this.status = httpStatus.toString();
		this.reason = reason;
		this.message = message;
		this.timestamp = DateTimeUtils.dateTimeToString(LocalDateTime.now());
	}

}
