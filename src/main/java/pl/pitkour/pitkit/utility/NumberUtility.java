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
import com.github.pitcer.shorts.throwable.Throwables;

public final class NumberUtility
{
	private static final DecimalFormat THOUSANDS_SEPARATOR = new DecimalFormat();
	private static final Random RANDOM = ThreadLocalRandom.current();

	private NumberUtility()
	{}

	public static Optional<Byte> parseByte(String string)
	{
		return Throwables.tryCatch(() -> Byte.parseByte(string));
	}

	public static Optional<Short> parseShort(String string)
	{
		return Throwables.tryCatch(() -> Short.parseShort(string));
	}

	public static Optional<Integer> parseInteger(String string)
	{
		return Throwables.tryCatch(() -> Integer.parseInt(string));
	}

	public static Optional<Long> parseLong(String string)
	{
		return Throwables.tryCatch(() -> Long.parseLong(string));
	}

	public static Optional<Float> parseFloat(String string)
	{
		return Throwables.tryCatch(() -> Float.parseFloat(string));
	}

	public static Optional<Double> parseDouble(String string)
	{
		return Throwables.tryCatch(() -> Double.parseDouble(string));
	}

	public static String separateThousands(long number)
	{
		return THOUSANDS_SEPARATOR.format(number);
	}

	public static String separateThousands(double number)
	{
		return THOUSANDS_SEPARATOR.format(number);
	}

	public static boolean chance(double percent)
	{
		int randomInteger = random(100_000);
		percent *= 1_000D;
		return randomInteger <= percent;
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