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
package pl.pitkour.pitkit.text.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.github.pitcer.shorts.Conditions;
import com.github.pitcer.shorts.Loops;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.pitkour.pitkit.text.Text;
import pl.pitkour.pitkit.text.message.Message.MessageBuilder;
import pl.pitkour.pitkit.text.message.event.ClickEvent;
import pl.pitkour.pitkit.text.message.event.HoverEvent;
import pl.pitkour.pitkit.text.message.event.action.ClickAction;
import pl.pitkour.pitkit.text.message.event.action.HoverAction;
import pl.pitkour.pitkit.utility.Buildable;
import pl.pitkour.pitkit.utility.Builder;
import pl.pitkour.pitkit.utility.NumberUtility;
import pl.pitkour.pitkit.utility.TimeUtility;
import static pl.pitkour.pitkit.text.Text.HIGHLIGHTED_COLOR;
import static pl.pitkour.pitkit.text.Text.PREFIX;
import static pl.pitkour.pitkit.text.Text.PREFIX_COLOR;
import static pl.pitkour.pitkit.text.Text.REGULAR_COLOR;

public final class Message implements Buildable<MessageBuilder>, Serializable
{
	private List<MessagePart> parts = new ArrayList<>();
	private transient MessagePart currentPart;

	public Message()
	{
		this(new Text());
	}

	public Message(BaseComponent... baseComponents)
	{
		Loops.forEach(baseComponents, this::addText);
	}

	public Message(String text)
	{
		addText(text);
	}

	public Message(Text text)
	{
		addText(text);
	}

	public static Message empty()
	{
		return new Message();
	}

	public static Message of(BaseComponent... baseComponents)
	{
		return new Message(baseComponents);
	}

	public static Message of(String text)
	{
		return new Message(text);
	}

	public static Message of(Text text)
	{
		return new Message(text);
	}

	public static MessageBuilder builder()
	{
		return new MessageBuilder();
	}

	public static MessageBuilder builder(BaseComponent... baseComponents)
	{
		return new MessageBuilder(baseComponents);
	}

	public static MessageBuilder builder(String text)
	{
		return new MessageBuilder(text);
	}

	public static MessageBuilder builder(Text text)
	{
		return new MessageBuilder(text);
	}

	@Override
	public MessageBuilder toBuilder()
	{
		return new MessageBuilder(this);
	}

	public void sendChat(Player receiver)
	{
		sendPacket(receiver, ChatMessageType.CHAT);
	}

	public void sendGameInfo(Player receiver)
	{
		sendPacket(receiver, ChatMessageType.GAME_INFO);
	}

	public void sendSystem(Player receiver)
	{
		sendPacket(receiver, ChatMessageType.SYSTEM);
	}

	private void sendPacket(Player receiver, ChatMessageType messageType)
	{
		IChatBaseComponent baseComponent = ChatSerializer.a(toString());
		PacketPlayOutChat packet = new PacketPlayOutChat(baseComponent, messageType);
		CraftPlayer craftPlayer = (CraftPlayer)receiver;
		EntityPlayer entityPlayer = craftPlayer.getHandle();
		PlayerConnection connection = entityPlayer.playerConnection;
		connection.sendPacket(packet);
	}

	public BaseComponent[] toBaseComponents()
	{
		return ComponentSerializer.parse(toString());
	}

	@Override
	public String toString()
	{
		JsonArray jsonArray = new JsonArray();
		this.parts.forEach(part -> jsonArray.add(part.toJsonObject()));
		return jsonArray.toString();
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
		Message that = (Message)object;
		return this.parts.equals(that.parts);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.parts);
	}

	public void addDate(long millis)
	{
		String date = TimeUtility.getDate(millis);
		addHighlightedColored(date);
	}

	public void addTime(long millis)
	{
		String time = TimeUtility.getTime(millis);
		addHighlightedColored(time);
	}

	public void addNumber(long number)
	{
		String separatedThousands = NumberUtility.separateThousands(number);
		addHighlightedColored(separatedThousands);
	}

	public void addNumber(double floatingPointNumber)
	{
		String separatedThousands = NumberUtility.separateThousands(floatingPointNumber);
		addHighlightedColored(separatedThousands);
	}

	public void addInBrackets(String text)
	{
		addRegularColored("(");
		addHighlightedColored(text);
		addRegularColored(")");
	}

	public void addInBrackets(Text text)
	{
		addRegularColored("(");
		addHighlightedColored(text);
		addRegularColored(")");
	}

	public void addRegularColored(String text)
	{
		addColored(text, REGULAR_COLOR);
	}

	public void addRegularColored(Text text)
	{
		addColored(text, REGULAR_COLOR);
	}

	public void addHighlightedColored(String text)
	{
		addColored(text, HIGHLIGHTED_COLOR);
	}

	public void addHighlightedColored(Text text)
	{
		addColored(text, HIGHLIGHTED_COLOR);
	}

	public void addPrefix()
	{
		addColored(PREFIX, PREFIX_COLOR);
	}

	private void addColored(String text, ChatColor color)
	{
		addText(text);
		this.currentPart.setColor(color);
	}

	private void addColored(Text text, ChatColor color)
	{
		addText(text);
		this.currentPart.setColor(color);
	}

	public void addLine()
	{
		addText("\n");
	}

	public void addSpace()
	{
		addText(" ");
	}

	public void addText(BaseComponent text)
	{
		addPart(new MessagePart(text));
	}

	public void addText(String text)
	{
		addPart(new MessagePart(text));
	}

	public void addText(Text text)
	{
		addPart(new MessagePart(text));
	}

	private void addPart(MessagePart part)
	{
		this.currentPart = part;
		this.parts.add(part);
	}

	public int getPartsCount()
	{
		return this.parts.size();
	}

	public MessagePart getCurrentPart()
	{
		return this.currentPart;
	}

	public final class MessagePart
	{
		private Text text;
		private ChatColor color;
		private boolean magicFormat;
		private boolean boldFormat;
		private boolean strikethroughFormat;
		private boolean underlineFormat;
		private boolean italicFormat;
		private HoverEvent hoverEvent;
		private ClickEvent clickEvent;

		private MessagePart(BaseComponent baseComponent)
		{
			this(new Text(baseComponent.getInsertion()));
			setColor(baseComponent.getColor());
			this.magicFormat = baseComponent.isObfuscated();
			this.boldFormat = baseComponent.isBold();
			this.strikethroughFormat = baseComponent.isStrikethrough();
			this.underlineFormat = baseComponent.isUnderlined();
			this.italicFormat = baseComponent.isItalic();
			net.md_5.bungee.api.chat.HoverEvent hoverEvent = baseComponent.getHoverEvent();
			this.hoverEvent = new HoverEvent(HoverAction.get(hoverEvent.getAction()), new Text(hoverEvent.getValue()));
			net.md_5.bungee.api.chat.ClickEvent clickEvent = baseComponent.getClickEvent();
			this.clickEvent = new ClickEvent(ClickAction.get(clickEvent.getAction()), new Text(clickEvent.getValue()));
		}

		private MessagePart(String text)
		{
			this(new Text(text));
		}

		private MessagePart(Text text)
		{
			this.text = text;
		}

		private JsonObject toJsonObject()
		{
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("text", this.text.toString());
			Conditions.ifThen(this.color != null, () -> jsonObject.addProperty("color", this.color.name().toLowerCase()));
			Conditions.ifThen(this.magicFormat, () -> jsonObject.addProperty("magic", true));
			Conditions.ifThen(this.boldFormat, () -> jsonObject.addProperty("bold", true));
			Conditions.ifThen(this.strikethroughFormat, () -> jsonObject.addProperty("strikethrough", true));
			Conditions.ifThen(this.underlineFormat, () -> jsonObject.addProperty("underline", true));
			Conditions.ifThen(this.italicFormat, () -> jsonObject.addProperty("italic", true));
			if(this.hoverEvent != null)
			{
				JsonObject jsonHoverEvent = new JsonObject();
				jsonHoverEvent.addProperty("action", this.hoverEvent.getAction().getName());
				jsonHoverEvent.addProperty("value", this.hoverEvent.getValue().toString());
				jsonObject.add("hoverEvent", jsonHoverEvent);
			}
			if(this.clickEvent != null)
			{
				JsonObject jsonClickEvent = new JsonObject();
				jsonClickEvent.addProperty("action", this.clickEvent.getAction().getName());
				jsonClickEvent.addProperty("value", this.clickEvent.getValue().toString());
				jsonObject.add("clickEvent", jsonClickEvent);
			}
			return jsonObject;
		}

		public Text getText()
		{
			return this.text;
		}

		public void setText(Text text)
		{
			this.text = text;
		}

		public ChatColor getColor()
		{
			return this.color;
		}

		public void setColor(net.md_5.bungee.api.ChatColor color)
		{
			this.color = ChatColor.valueOf(color.name());
		}

		public void setColor(ChatColor color)
		{
			this.color = color;
		}

		public boolean isMagicFormat()
		{
			return this.magicFormat;
		}

		public void setMagicFormat(boolean magicFormat)
		{
			this.magicFormat = magicFormat;
		}

		public boolean isBoldFormat()
		{
			return this.boldFormat;
		}

		public void setBoldFormat(boolean boldFormat)
		{
			this.boldFormat = boldFormat;
		}

		public boolean isStrikethroughFormat()
		{
			return this.strikethroughFormat;
		}

		public void setStrikethroughFormat(boolean strikethroughFormat)
		{
			this.strikethroughFormat = strikethroughFormat;
		}

		public boolean isUnderlineFormat()
		{
			return this.underlineFormat;
		}

		public void setUnderlineFormat(boolean underlineFormat)
		{
			this.underlineFormat = underlineFormat;
		}

		public boolean isItalicFormat()
		{
			return this.italicFormat;
		}

		public void setItalicFormat(boolean italicFormat)
		{
			this.italicFormat = italicFormat;
		}

		public HoverEvent getHoverEvent()
		{
			return this.hoverEvent;
		}

		public void setHoverEvent(HoverAction action, Text text)
		{
			this.hoverEvent = new HoverEvent(action, text);
		}

		public void setHoverEvent(HoverAction action, String value)
		{
			this.hoverEvent = new HoverEvent(action, value);
		}

		public void setHoverEvent(HoverEvent hoverEvent)
		{
			this.hoverEvent = hoverEvent;
		}

		public ClickEvent getClickEvent()
		{
			return this.clickEvent;
		}

		public void setClickEvent(ClickAction action, Text text)
		{
			this.clickEvent = new ClickEvent(action, text);
		}

		public void setClickEvent(ClickAction action, String value)
		{
			this.clickEvent = new ClickEvent(action, value);
		}

		public void setClickEvent(ClickEvent clickEvent)
		{
			this.clickEvent = clickEvent;
		}
	}

	public final static class MessageBuilder implements Builder<Message>
	{
		private Message message;

		private MessageBuilder()
		{
			this(new Message());
		}

		private MessageBuilder(BaseComponent[] baseComponents)
		{
			this(new Message(baseComponents));
		}

		private MessageBuilder(String text)
		{
			this(new Message(text));
		}

		private MessageBuilder(Text text)
		{
			this(new Message(text));
		}

		private MessageBuilder(Message message)
		{
			this.message = message;
		}

		public MessageBuilder date(long millis)
		{
			this.message.addDate(millis);
			return this;
		}

		public MessageBuilder time(long millis)
		{
			this.message.addTime(millis);
			return this;
		}

		public MessageBuilder number(long number)
		{
			this.message.addNumber(number);
			return this;
		}

		public MessageBuilder number(double floatingPointNumber)
		{
			this.message.addNumber(floatingPointNumber);
			return this;
		}

		public MessageBuilder brackets(String text)
		{
			this.message.addInBrackets(text);
			return this;
		}

		public MessageBuilder brackets(Text text)
		{
			this.message.addInBrackets(text);
			return this;
		}

		public MessageBuilder regular(String text)
		{
			this.message.addRegularColored(text);
			return this;
		}

		public MessageBuilder regular(Text text)
		{
			this.message.addRegularColored(text);
			return this;
		}

		public MessageBuilder highlighted(String text)
		{
			this.message.addHighlightedColored(text);
			return this;
		}

		public MessageBuilder highlighted(Text text)
		{
			this.message.addHighlightedColored(text);
			return this;
		}

		public MessageBuilder prefix()
		{
			this.message.addPrefix();
			return this;
		}

		public MessageBuilder line()
		{
			this.message.addLine();
			return this;
		}

		public MessageBuilder space()
		{
			this.message.addSpace();
			return this;
		}

		public MessageBuilder text(BaseComponent baseComponent)
		{
			this.message.addText(baseComponent);
			return this;
		}

		public MessageBuilder text(String text)
		{
			this.message.addText(text);
			return this;
		}

		public MessageBuilder text(Text text)
		{
			this.message.addText(text);
			return this;
		}

		public MessageBuilder color(net.md_5.bungee.api.ChatColor color)
		{
			this.message.getCurrentPart().setColor(color);
			return this;
		}

		public MessageBuilder color(ChatColor color)
		{
			this.message.getCurrentPart().setColor(color);
			return this;
		}

		public MessageBuilder magic()
		{
			this.message.getCurrentPart().setMagicFormat(true);
			return this;
		}

		public MessageBuilder bold()
		{
			this.message.getCurrentPart().setBoldFormat(true);
			return this;
		}

		public MessageBuilder strikethrough()
		{
			this.message.getCurrentPart().setStrikethroughFormat(true);
			return this;
		}

		public MessageBuilder underline()
		{
			this.message.getCurrentPart().setUnderlineFormat(true);
			return this;
		}

		public MessageBuilder italic()
		{
			this.message.getCurrentPart().setItalicFormat(true);
			return this;
		}

		public MessageBuilder hover(HoverAction action, Text text)
		{
			this.message.getCurrentPart().setHoverEvent(action, text);
			return this;
		}

		public MessageBuilder hover(HoverAction action, String value)
		{
			this.message.getCurrentPart().setHoverEvent(action, value);
			return this;
		}

		public MessageBuilder hover(HoverEvent hoverEvent)
		{
			this.message.getCurrentPart().setHoverEvent(hoverEvent);
			return this;
		}

		public MessageBuilder click(ClickAction action, Text text)
		{
			this.message.getCurrentPart().setClickEvent(action, text);
			return this;
		}

		public MessageBuilder click(ClickAction action, String value)
		{
			this.message.getCurrentPart().setClickEvent(action, value);
			return this;
		}

		public MessageBuilder click(ClickEvent clickEvent)
		{
			this.message.getCurrentPart().setClickEvent(clickEvent);
			return this;
		}

		@Override
		public Message build()
		{
			return this.message;
		}
	}
}
