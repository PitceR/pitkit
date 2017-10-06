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
import java.util.Collections;
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
import pl.pitkour.pitkit.text.message.event.ClickEvent;
import pl.pitkour.pitkit.text.message.event.HoverEvent;
import pl.pitkour.pitkit.text.message.event.action.ClickAction;
import pl.pitkour.pitkit.text.message.event.action.HoverAction;
import pl.pitkour.pitkit.utility.Builder;
import pl.pitkour.pitkit.utility.NumberUtility;
import pl.pitkour.pitkit.utility.TimeUtility;
import static pl.pitkour.pitkit.text.Text.ERROR_COLOR;
import static pl.pitkour.pitkit.text.Text.ERROR_HIGHLIGHTED_COLOR;
import static pl.pitkour.pitkit.text.Text.HIGHLIGHTED_COLOR;
import static pl.pitkour.pitkit.text.Text.PREFIX;
import static pl.pitkour.pitkit.text.Text.PREFIX_COLOR;
import static pl.pitkour.pitkit.text.Text.REGULAR_COLOR;

public final class Message implements Serializable
{
	private List<MessagePart> parts = new ArrayList<>();
	private transient MessagePart currentPart;

	private Message()
	{
		this(Text.empty());
	}

	private Message(Message message)
	{
		this.parts = Loops.transform(message.parts, MessagePart::new);
		this.currentPart = message.currentPart;
	}

	private Message(BaseComponent[] baseComponents)
	{
		Loops.forEach(baseComponents, baseComponent -> addPart(new MessagePart(baseComponent)));
	}

	private Message(String text)
	{
		addPart(new MessagePart(text));
	}

	private Message(Text text)
	{
		addPart(new MessagePart(text));
	}

	public static Message empty()
	{
		return new Message();
	}

	public static Message of(Message message)
	{
		Objects.requireNonNull(message, "message must not be null");
		return new Message(message);
	}

	public static Message of(BaseComponent... baseComponents)
	{
		Objects.requireNonNull(baseComponents, "baseComponents must not be null");
		return new Message(baseComponents);
	}

	public static Message of(String text)
	{
		Objects.requireNonNull(text, "text must not be null");
		return new Message(text);
	}

	public static Message of(Text text)
	{
		Objects.requireNonNull(text, "text must not be null");
		return new Message(text);
	}

	public static MessageBuilder builder()
	{
		return new MessageBuilder(new Message());
	}

	public static MessageBuilder builder(Message message)
	{
		Objects.requireNonNull(message, "message must not be null");
		return new MessageBuilder(new Message(message));
	}

	public static MessageBuilder builder(BaseComponent... baseComponents)
	{
		Objects.requireNonNull(baseComponents, "baseComponents must not be null");
		return new MessageBuilder(new Message(baseComponents));
	}

	public static MessageBuilder builder(String text)
	{
		Objects.requireNonNull(text, "text must not be null");
		return new MessageBuilder(new Message(text));
	}

	public static MessageBuilder builder(Text text)
	{
		Objects.requireNonNull(text, "text must not be null");
		return new MessageBuilder(new Message(text));
	}

	private void addPart(MessagePart part)
	{
		this.currentPart = part;
		this.parts.add(part);
	}

	public void sendChat(Player receiver)
	{
		Objects.requireNonNull(receiver, "receiver must not be null");
		sendPacket(receiver, ChatMessageType.CHAT);
	}

	public void sendGameInfo(Player receiver)
	{
		Objects.requireNonNull(receiver, "receiver must not be null");
		sendPacket(receiver, ChatMessageType.GAME_INFO);
	}

	public void sendSystem(Player receiver)
	{
		Objects.requireNonNull(receiver, "receiver must not be null");
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

	public List<MessagePart> getParts()
	{
		return Collections.unmodifiableList(this.parts);
	}

	public final static class MessagePart implements Serializable
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

		private MessagePart(MessagePart part)
		{
			this(Text.of(part.text));
			this.color = part.color;
			this.magicFormat = part.magicFormat;
			this.boldFormat = part.boldFormat;
			this.strikethroughFormat = part.strikethroughFormat;
			this.underlineFormat = part.underlineFormat;
			this.italicFormat = part.italicFormat;
			Conditions.ifThen(part.hoverEvent != null, () -> this.hoverEvent = new HoverEvent(part.hoverEvent));
			Conditions.ifThen(part.clickEvent != null, () -> this.clickEvent = new ClickEvent(part.clickEvent));
		}

		private MessagePart(BaseComponent baseComponent)
		{
			this(Text.of(baseComponent.getInsertion()));
			this.color = ChatColor.valueOf(baseComponent.getColor().name());
			this.magicFormat = baseComponent.isObfuscated();
			this.boldFormat = baseComponent.isBold();
			this.strikethroughFormat = baseComponent.isStrikethrough();
			this.underlineFormat = baseComponent.isUnderlined();
			this.italicFormat = baseComponent.isItalic();
			net.md_5.bungee.api.chat.HoverEvent hoverEvent = baseComponent.getHoverEvent();
			this.hoverEvent = new HoverEvent(HoverAction.get(hoverEvent.getAction()), Text.of(hoverEvent.getValue()));
			net.md_5.bungee.api.chat.ClickEvent clickEvent = baseComponent.getClickEvent();
			this.clickEvent = new ClickEvent(ClickAction.get(clickEvent.getAction()), Text.of(clickEvent.getValue()));
		}

		private MessagePart(String text)
		{
			this(Text.of(text));
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
			MessagePart that = (MessagePart)object;
			return this.text.equals(that.text) && this.color == that.color && this.magicFormat == that.magicFormat && this.boldFormat == that.boldFormat && this.strikethroughFormat == that.strikethroughFormat && this.underlineFormat == that.underlineFormat && this.italicFormat == that.italicFormat && this.hoverEvent.equals(that.hoverEvent) && this.clickEvent.equals(that.clickEvent);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(this.text, this.color, this.magicFormat, this.boldFormat, this.strikethroughFormat, this.underlineFormat, this.italicFormat, this.hoverEvent, this.clickEvent);
		}

		@Override
		public String toString()
		{
			return "MessagePart{" + "text=" + this.text + ", color=" + this.color + ", magicFormat=" + this.magicFormat + ", boldFormat=" + this.boldFormat + ", strikethroughFormat=" + this.strikethroughFormat + ", underlineFormat=" + this.underlineFormat + ", italicFormat=" + this.italicFormat + ", hoverEvent=" + this.hoverEvent + ", clickEvent=" + this.clickEvent + '}';
		}

		public Text getText()
		{
			return this.text;
		}

		public ChatColor getColor()
		{
			return this.color;
		}

		public boolean isMagicFormat()
		{
			return this.magicFormat;
		}

		public boolean isBoldFormat()
		{
			return this.boldFormat;
		}

		public boolean isStrikethroughFormat()
		{
			return this.strikethroughFormat;
		}

		public boolean isUnderlineFormat()
		{
			return this.underlineFormat;
		}

		public boolean isItalicFormat()
		{
			return this.italicFormat;
		}

		public HoverEvent getHoverEvent()
		{
			return this.hoverEvent;
		}

		public ClickEvent getClickEvent()
		{
			return this.clickEvent;
		}
	}

	public final static class MessageBuilder implements Builder<Message>
	{
		private Message message;

		private MessageBuilder(Message message)
		{
			this.message = message;
		}

		public MessageBuilder date(long millis)
		{
			String date = TimeUtility.getDate(millis);
			return highlighted(date);
		}

		public MessageBuilder time(long millis)
		{
			String time = TimeUtility.getTime(millis);
			return highlighted(time);
		}

		public MessageBuilder number(long number)
		{
			String separatedThousands = NumberUtility.separateThousands(number);
			return highlighted(separatedThousands);
		}

		public MessageBuilder number(double floatingPointNumber)
		{
			String separatedThousands = NumberUtility.separateThousands(floatingPointNumber);
			return highlighted(separatedThousands);
		}

		public MessageBuilder brackets(String text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return brackets(Text.of(text));
		}

		public MessageBuilder brackets(Text text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return regular("(").highlighted(text).regular(")");
		}

		public MessageBuilder regular(String text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return regular(Text.of(text));
		}

		public MessageBuilder regular(Text text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return text(text).color(REGULAR_COLOR);
		}

		public MessageBuilder highlighted(String text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return highlighted(Text.of(text));
		}

		public MessageBuilder highlighted(Text text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return text(text).color(HIGHLIGHTED_COLOR);
		}

		public MessageBuilder error(String text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return error(Text.of(text));
		}

		public MessageBuilder error(Text text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return color(ERROR_COLOR).text(text);
		}

		public MessageBuilder errorHighlighted(String text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return errorHighlighted(Text.of(text));
		}

		public MessageBuilder errorHighlighted(Text text)
		{
			Objects.requireNonNull(text, "text must not be null");
			return color(ERROR_HIGHLIGHTED_COLOR).text(text);
		}

		public MessageBuilder prefix()
		{
			return text(PREFIX).color(PREFIX_COLOR);
		}

		public MessageBuilder line()
		{
			return text("\n");
		}

		public MessageBuilder space()
		{
			return text(" ");
		}

		public MessageBuilder text(BaseComponent baseComponent)
		{
			Objects.requireNonNull(baseComponent, "baseComponent must not be null");
			this.message.addPart(new MessagePart(baseComponent));
			return this;
		}

		public MessageBuilder text(String text)
		{
			Objects.requireNonNull(text, "text must not be null");
			this.message.addPart(new MessagePart(text));
			return this;
		}

		public MessageBuilder text(Text text)
		{
			Objects.requireNonNull(text, "text must not be null");
			this.message.addPart(new MessagePart(text));
			return this;
		}

		public MessageBuilder color(net.md_5.bungee.api.ChatColor color)
		{
			Objects.requireNonNull(color, "color must not be null");
			return color(ChatColor.valueOf(color.name()));
		}

		public MessageBuilder color(ChatColor color)
		{
			Objects.requireNonNull(color, "color must not be null");
			this.message.currentPart.color = color;
			return this;
		}

		public MessageBuilder magic()
		{
			return magic(true);
		}

		public MessageBuilder magic(boolean magic)
		{
			this.message.currentPart.magicFormat = magic;
			return this;
		}

		public MessageBuilder bold()
		{
			return bold(true);
		}

		public MessageBuilder bold(boolean bold)
		{
			this.message.currentPart.boldFormat = bold;
			return this;
		}

		public MessageBuilder strikethrough()
		{
			return strikethrough(true);
		}

		public MessageBuilder strikethrough(boolean strikethrough)
		{
			this.message.currentPart.strikethroughFormat = strikethrough;
			return this;
		}

		public MessageBuilder underline()
		{
			return underline(true);
		}

		public MessageBuilder underline(boolean underline)
		{
			this.message.currentPart.underlineFormat = underline;
			return this;
		}

		public MessageBuilder italic()
		{
			return italic(true);
		}

		public MessageBuilder italic(boolean italic)
		{
			this.message.currentPart.italicFormat = italic;
			return this;
		}

		public MessageBuilder hover(HoverAction action, Text text)
		{
			Objects.requireNonNull(action, "action must not be null");
			Objects.requireNonNull(text, "text must not be null");
			return hover(new HoverEvent(action, text));
		}

		public MessageBuilder hover(HoverAction action, String value)
		{
			Objects.requireNonNull(action, "action must not be null");
			Objects.requireNonNull(value, "value must not be null");
			return hover(new HoverEvent(action, value));
		}

		public MessageBuilder hover(HoverEvent event)
		{
			Objects.requireNonNull(event, "event must not be null");
			this.message.currentPart.hoverEvent = event;
			return this;
		}

		public MessageBuilder click(ClickAction action, Text text)
		{
			Objects.requireNonNull(action, "action must not be null");
			Objects.requireNonNull(text, "text must not be null");
			return click(new ClickEvent(action, text));
		}

		public MessageBuilder click(ClickAction action, String value)
		{
			Objects.requireNonNull(action, "action must not be null");
			Objects.requireNonNull(value, "value must not be null");
			return click(new ClickEvent(action, value));
		}

		public MessageBuilder click(ClickEvent event)
		{
			Objects.requireNonNull(event, "event must not be null");
			this.message.currentPart.clickEvent = event;
			return this;
		}

		@Override
		public Message build()
		{
			return this.message;
		}
	}
}
