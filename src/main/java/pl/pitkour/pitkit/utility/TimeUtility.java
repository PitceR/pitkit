package pl.pitkour.pitkit.utility;

import java.time.Instant;
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
		return DATE_FORMATTER.format(instant);
	}

	public static String getTime(long millis)
	{
		Instant instant = Instant.ofEpochMilli(millis);
		return TIME_FORMATTER.format(instant);
	}
}
