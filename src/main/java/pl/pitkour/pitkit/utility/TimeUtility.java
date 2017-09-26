package pl.pitkour.pitkit.utility;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class TimeUtility
{
	public static final ZoneId TIME_ZONE = ZoneId.of("Europe/Warsaw");
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	private TimeUtility()
	{}

	public static String getDate(long millis)
	{
		Instant instant = Instant.ofEpochMilli(millis);
		LocalDateTime time = LocalDateTime.ofInstant(instant, TIME_ZONE);
		return time.format(DATE_FORMATTER);
	}

	public static String getTime(long millis)
	{
		Instant instant = Instant.ofEpochMilli(millis);
		LocalDateTime time = LocalDateTime.ofInstant(instant, TIME_ZONE);
		return time.format(TIME_FORMATTER);
	}
}
