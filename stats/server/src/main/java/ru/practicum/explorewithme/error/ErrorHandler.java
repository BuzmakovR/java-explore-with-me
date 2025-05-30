package ru.practicum.explorewithme.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.exception.ValidationException;

@RestControllerAdvice
public class ErrorHandler {

	private static final String REASON_BAD_REQUEST = "Integrity constraint has been violated.";

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handlerValidationException(final ValidationException e) {
		return new ApiError(HttpStatus.BAD_REQUEST, REASON_BAD_REQUEST, e.getMessage());
	}
}
