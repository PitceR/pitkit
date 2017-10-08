/*
 * Copyright 2017 PitceR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
	{
		throw new UnsupportedOperationException("cannot create instance of utility class");
	}

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
