package ru.practicum.explorewithme.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

	public static LocalDateTime stringToDateTime(String strDateTime) {
		if (strDateTime == null || strDateTime.isBlank()) {
			return null;
		}
		return LocalDateTime.parse(strDateTime, dateTimeFormatter);
	}

	public static String dateTimeToString(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return "";
		}
		return localDateTime.format(dateTimeFormatter);
	}
}
