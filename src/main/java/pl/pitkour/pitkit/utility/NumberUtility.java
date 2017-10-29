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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class NumberUtility
{
	private static final DecimalFormat THOUSANDS_SEPARATOR = new DecimalFormat();
	private static final Random RANDOM = ThreadLocalRandom.current();

	private NumberUtility()
	{
		throw new UnsupportedOperationException("cannot create instance of utility class");
	}

	public static Optional<Byte> parseByte(String numberString)
	{
		try
		{
			byte parsed = Byte.parseByte(numberString);
			return Optional.of(parsed);
		}
		catch(NumberFormatException ignored)
		{
			return Optional.empty();
		}
	}

	public static Optional<Short> parseShort(String numberString)
	{
		try
		{
			short parsed = Short.parseShort(numberString);
			return Optional.of(parsed);
		}
		catch(NumberFormatException ignored)
		{
			return Optional.empty();
		}
	}

	public static Optional<Integer> parseInteger(String numberString)
	{
		try
		{
			int parsed = Integer.parseInt(numberString);
			return Optional.of(parsed);
		}
		catch(NumberFormatException ignored)
		{
			return Optional.empty();
		}
	}

	public static Optional<Long> parseLong(String numberString)
	{
		try
		{
			long parsed = Long.parseLong(numberString);
			return Optional.of(parsed);
		}
		catch(NumberFormatException ignored)
		{
			return Optional.empty();
		}
	}

	public static Optional<Float> parseFloat(String numberString)
	{
		try
		{
			float parsed = Float.parseFloat(numberString);
			return Optional.of(parsed);
		}
		catch(NumberFormatException ignored)
		{
			return Optional.empty();
		}
	}

	public static Optional<Double> parseDouble(String numberString)
	{
		try
		{
			double parsed = Double.parseDouble(numberString);
			return Optional.of(parsed);
		}
		catch(NumberFormatException ignored)
		{
			return Optional.empty();
		}
	}

	public static String separateThousands(long number)
	{
		return THOUSANDS_SEPARATOR.format(number);
	}

	public static String separateThousands(double number)
	{
		return THOUSANDS_SEPARATOR.format(number);
	}

	public static boolean equals(float float1, float float2)
	{
		return Float.floatToIntBits(float1) == Float.floatToIntBits(float2);
	}

	public static boolean equals(double double1, double double2)
	{
		return Double.doubleToLongBits(double1) == Double.doubleToLongBits(double2);
	}

	public static boolean hasChance(double percent)
	{
		int randomInteger = random(100_000);
		return randomInteger <= percent * 1_000.0;
	}

	public static int random(int high)
	{
		return random(0, high);
	}

	public static int random(int low, int high)
	{
		return RANDOM.nextInt(high - low + 1) + low;
	}

	public static int getLenght(long number)
	{
		return (int)Math.log10(number) + 1;
	}

	static
	{
		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
		symbols.setGroupingSeparator(' ');
		THOUSANDS_SEPARATOR.applyPattern("#,###");
		THOUSANDS_SEPARATOR.setDecimalFormatSymbols(symbols);
	}
}