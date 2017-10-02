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
		return new Text(text);
	}

	public static Text of(BaseComponent... baseComponents)
	{
		return new Text(baseComponents);
	}

	public static Text of(String text)
	{
		return new Text(text);
	}

	public static TextBuilder builder()
	{
		return new TextBuilder(new Text());
	}

	public static TextBuilder builder(Text text)
	{
		return new TextBuilder(new Text(text));
	}

	public static TextBuilder builder(BaseComponent... baseComponents)
	{
		return new TextBuilder(new Text(baseComponents));
	}

	public static TextBuilder builder(String text)
	{
		return new TextBuilder(new Text(text));
	}

	public static String colorize(String text)
	{
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static String uncolorize(String text)
	{
		return ChatColor.stripColor(text);
	}

	public void send(CommandSender receiver)
	{
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

	private void addDate(long millis)
	{
		String date = TimeUtility.getDate(millis);
		addHighlightedColored(date);
	}

	private void addTime(long millis)
	{
		String time = TimeUtility.getTime(millis);
		addHighlightedColored(time);
	}

	private void addNumber(long number)
	{
		String separatedThousands = NumberUtility.separateThousands(number);
		addHighlightedColored(separatedThousands);
	}

	private void addNumber(double floatingPointNumber)
	{
		String separatedThousands = NumberUtility.separateThousands(floatingPointNumber);
		addHighlightedColored(separatedThousands);
	}

	private void addInBrackets(Text text)
	{
		addInBrackets(text.toString());
	}

	private void addInBrackets(String text)
	{
		addRegularColored("(");
		addHighlightedColored(text);
		addRegularColored(")");
	}

	private void addRegularColored(Text text)
	{
		addRegularColored(text.toString());
	}

	private void addRegularColored(String text)
	{
		addColor(REGULAR_COLOR);
		addText(text);
	}

	private void addHighlightedColored(Text text)
	{
		addHighlightedColored(text.toString());
	}

	private void addHighlightedColored(String text)
	{
		addColor(HIGHLIGHTED_COLOR);
		addText(text);
	}

	private void addErrorColored(Text text)
	{
		addErrorColored(text.toString());
	}

	private void addErrorColored(String text)
	{
		addColor(ERROR_COLOR);
		addText(text);
	}

	private void addErrorHighlightedColored(Text text)
	{
		addErrorHighlightedColored(text.toString());
	}

	private void addErrorHighlightedColored(String text)
	{
		addColor(ERROR_HIGHLIGHTED_COLOR);
		addText(text);
	}

	private void addColor(net.md_5.bungee.api.ChatColor color)
	{
		addColor(ChatColor.valueOf(color.name()));
	}

	private void addColor(ChatColor color)
	{
		addText(color.toString());
	}

	private void addPrefix()
	{
		addText(PREFIX);
	}

	private void addText(Text text)
	{
		addText(text.toString());
	}

	private void addText(String text)
	{
		this.text.append(text);
	}

	private void addLine()
	{
		this.text.append('\n');
	}

	private void addSpace()
	{
		this.text.append(' ');
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
			this.text.addDate(millis);
			return this;
		}

		public TextBuilder time(long millis)
		{
			this.text.addTime(millis);
			return this;
		}

		public TextBuilder number(long number)
		{
			this.text.addNumber(number);
			return this;
		}

		public TextBuilder number(double floatingPointNumber)
		{
			this.text.addNumber(floatingPointNumber);
			return this;
		}

		public TextBuilder brackets(Text text)
		{
			this.text.addInBrackets(text);
			return this;
		}

		public TextBuilder brackets(String text)
		{
			this.text.addInBrackets(text);
			return this;
		}

		public TextBuilder regular(Text text)
		{
			this.text.addRegularColored(text);
			return this;
		}

		public TextBuilder regular(String text)
		{
			this.text.addRegularColored(text);
			return this;
		}

		public TextBuilder highlighted(Text text)
		{
			this.text.addHighlightedColored(text);
			return this;
		}

		public TextBuilder highlighted(String text)
		{
			this.text.addHighlightedColored(text);
			return this;
		}

		public TextBuilder error(Text text)
		{
			this.text.addErrorColored(text);
			return this;
		}

		public TextBuilder error(String text)
		{
			this.text.addErrorColored(text);
			return this;
		}

		public TextBuilder errorHighlighted(Text text)
		{
			this.text.addErrorHighlightedColored(text);
			return this;
		}

		public TextBuilder errorHighlighted(String text)
		{
			this.text.addErrorHighlightedColored(text);
			return this;
		}

		public TextBuilder color(net.md_5.bungee.api.ChatColor color)
		{
			this.text.addColor(color);
			return this;
		}

		public TextBuilder color(ChatColor color)
		{
			this.text.addColor(color);
			return this;
		}

		public TextBuilder prefix()
		{
			this.text.addPrefix();
			return this;
		}

		public TextBuilder text(Text text)
		{
			this.text.addText(text);
			return this;
		}

		public TextBuilder text(String text)
		{
			this.text.addText(text);
			return this;
		}

		public TextBuilder line()
		{
			this.text.addLine();
			return this;
		}

		public TextBuilder space()
		{
			this.text.addSpace();
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
