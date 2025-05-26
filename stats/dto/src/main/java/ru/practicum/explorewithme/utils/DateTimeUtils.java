package ru.practicum.explorewithme.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
