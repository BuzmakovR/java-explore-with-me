package ru.practicum.explorewithme.exception;

public class NotFoundException extends RuntimeException {

	public NotFoundException(String message, Long id) {
		super(String.format(message, id));
	}
}
