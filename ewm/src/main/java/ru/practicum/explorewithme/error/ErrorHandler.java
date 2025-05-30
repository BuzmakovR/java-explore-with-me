package ru.practicum.explorewithme.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.exception.ConditionsNotMetException;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.exception.ValidationException;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

	private static final String REASON_CONFLICT = "Incorrectly made request.";
	private static final String REASON_BAD_REQUEST = "Integrity constraint has been violated.";
	private static final String REASON_NOT_FOUND = "The required object was not found.";


	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiError handlerConflictException(final ConflictException e) {
		return new ApiError(HttpStatus.CONFLICT, REASON_CONFLICT, e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiError handlerConditionsNotMetException(final ConditionsNotMetException e) {
		return new ApiError(HttpStatus.CONFLICT, REASON_CONFLICT, e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiError handlerNotFoundException(final NotFoundException e) {
		return new ApiError(HttpStatus.NOT_FOUND, REASON_NOT_FOUND, e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handlerValidationException(final ValidationException e) {
		return new ApiError(HttpStatus.BAD_REQUEST, REASON_BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handlerMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
		return new ApiError(
				HttpStatus.BAD_REQUEST,
				REASON_BAD_REQUEST,
				Arrays.stream(Objects.requireNonNull(e.getDetailMessageArguments()))
						.map(String::valueOf)
						.filter(s -> !(s.isBlank()))
						.collect(Collectors.joining(";")));
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handlerInternalServerException(final Throwable e) {
		return new ApiError(HttpStatus.BAD_REQUEST,
				REASON_BAD_REQUEST,
				"Произошла непредвиденная ошибка");
	}
}
