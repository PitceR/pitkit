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

package pl.pitkour.pitkit.text;

import java.io.Serializable;
import java.util.Objects;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pl.pitkour.pitkit.utility.Builder;
import pl.pitkour.pitkit.utility.NumberUtility;
import pl.pitkour.pitkit.utility.TimeUtility;

public final class Text implements Serializable
{
	public static final ChatColor REGULAR_COLOR = ChatColor.GRAY;
	public static final ChatColor HIGHLIGHTED_COLOR = ChatColor.BLUE;
	public static final ChatColor ERROR_COLOR = ChatColor.RED;
	public static final ChatColor ERROR_HIGHLIGHTED_COLOR = ChatColor.GRAY;
	public static final ChatColor PREFIX_COLOR = ChatColor.DARK_GRAY;
	public static final Text PREFIX = builder().color(PREFIX_COLOR).text("> ").build();
	private static final long serialVersionUID = -1827542141277789793L;
	private StringBuilder text;
	private boolean colored;
	private boolean uncolored;

	private Text()
	{
		this.text = new StringBuilder();
	}

	private Text(Text text)
	{
		this.text = new StringBuilder(text.text);
		this.colored = text.colored;
		this.uncolored = text.uncolored;
	}

	private Text(BaseComponent... baseComponents)
	{
		this(TextComponent.toLegacyText(baseComponents));
	}

	private Text(String text)
	{
		this.text = new StringBuilder(text);
	}

	public static Text empty()
	{
		return new Text();
	}

	public static Text of(Text text)
	{
		Objects.requireNonNull(text, "text must not be null");
		return new Text(text);
	}

	public static Text of(BaseComponent... baseComponents)
	{
		Objects.requireNonNull(baseComponents, "baseComponents must not be null");
		return new Text(baseComponents);
	}

	public static Text of(String text)
	{
		Objects.requireNonNull(text, "text must not be null");
		return new Text(text);
	}

	public static TextBuilder builder()
	{
		return new TextBuilder(new Text());
	}

	public static TextBuilder builder(Text text)
	{
		Objects.requireNonNull(text, "text must not be null");
		return new TextBuilder(new Text(text));
	}

	public static TextBuilder builder(BaseComponent... baseComponents)
	{
		Objects.requireNonNull(baseComponents, "baseComponents must not be null");
		return new TextBuilder(new Text(baseComponents));
	}

	public static TextBuilder builder(String text)
	{
		Objects.requireNonNull(text, "text must not be null");
		return new TextBuilder(new Text(text));
	}

	public static String colorize(String text)
	{
		Objects.requireNonNull(text, "text must not be null");
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static String uncolorize(String text)
	{
		Objects.requireNonNull(text, "text must not be null");
		return ChatColor.stripColor(text);
	}

	public void send(CommandSender receiver)
	{
		Objects.requireNonNull(receiver, "receiver must not be null");
		receiver.sendMessage(toString());
	}

	public BaseComponent[] toBaseComponents()
	{
		return TextComponent.fromLegacyText(toString());
	}

	@Override
	public String toString()
	{
		String text = this.text.toString();
		text = this.colored ? colorize(text) : text;
		text = this.uncolored ? uncolorize(text) : text;
		return text;
	}

	@Override
	public boolean equals(Object object)
	{
		if(this == object)
		{
			return true;
		}
		if(object == null || getClass() != object.getClass())
		{
			return false;
		}
		Text that = (Text)object;
		return this.colored == that.colored && this.uncolored == that.uncolored && this.text.toString().equals(that.text.toString());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.colored, this.uncolored, this.text);
	}

	public boolean isColored()
	{
		return this.colored;
	}

	public boolean isUncolored()
	{
		return this.uncolored;
	}

	public static final class TextBuilder implements Builder<Text>
	{
		private Text text;

		private TextBuilder(Text text)
		{
			this.text = text;
		}

		public TextBuilder date(long millis)
		{
			String date = TimeUtility.getDate(millis);
			return highlighted(date);
		}

		public TextBuilder time(long millis)
		{
			String time = TimeUtility.getTime(millis);
			return highlighted(time);
		}

		public TextBuilder number(long number)
		{
			String separatedThousands = NumberUtility.separateThousands(number);
			return highlighted(separatedThousands);
		}

		public TextBuilder number(double floatingPointNumber)
		{
			String separatedThousands = NumberUtility.separateThousands(floatingPointNumber);
			return highlighted(separatedThousands);
		}

		public TextBuilder brackets(Text text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return brackets(text.toString());
		}

		public TextBuilder brackets(String text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return regular("(").highlighted(text).regular(")");
		}

		public TextBuilder regular(Text text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return regular(text.toString());
		}

		public TextBuilder regular(String text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return color(REGULAR_COLOR).text(text);
		}

		public TextBuilder highlighted(Text text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return highlighted(text.toString());
		}

		public TextBuilder highlighted(String text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return color(HIGHLIGHTED_COLOR).text(text);
		}

		public TextBuilder error(Text text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return error(text.toString());
		}

		public TextBuilder error(String text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return color(ERROR_COLOR).text(text);
		}

		public TextBuilder errorHighlighted(Text text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return errorHighlighted(text.toString());
		}

		public TextBuilder errorHighlighted(String text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return color(ERROR_HIGHLIGHTED_COLOR).text(text);
		}

		public TextBuilder color(net.md_5.bungee.api.ChatColor color)
		{
			Objects.requireNonNull(color, "color must not be null");
			return color(ChatColor.valueOf(color.name()));
		}

		public TextBuilder color(ChatColor color)
		{
			Objects.requireNonNull(color, "color must not be null");
			return text(color.toString());
		}

		public TextBuilder prefix()
		{
			return text(PREFIX);
		}

		public TextBuilder line()
		{
			return text("\n");
		}

		public TextBuilder space()
		{
			return text(" ");
		}

		public TextBuilder text(Text text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return text(text.toString());
		}

		public TextBuilder text(String text)
		{
			Objects.requireNonNull(text, "text must not be null");
			this.text.text.append(text);
			return this;
		}

		public TextBuilder colored()
		{
			colored(true);
			return this;
		}

		public TextBuilder colored(boolean colored)
		{
			this.text.colored = colored;
			return this;
		}

		public TextBuilder uncolored()
		{
			uncolored(true);
			return this;
		}

		public TextBuilder uncolored(boolean uncolored)
		{
			this.text.uncolored = uncolored;
			return this;
		}

		@Override
		public Text build()
		{
			return this.text;
		}
	}
}
