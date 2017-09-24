package pl.pitkour.pitkit.text.message.event.action;

import net.md_5.bungee.api.chat.HoverEvent.Action;

public enum HoverAction
{
	SHOW_TEXT,
	SHOW_ITEM,
	SHOW_ENTITY,
	SHOW_ACHIEVEMENT;

	public static HoverAction get(Action action)
	{
		switch(action)
		{
			case SHOW_TEXT:
				return HoverAction.SHOW_TEXT;
			case SHOW_ITEM:
				return HoverAction.SHOW_ITEM;
			case SHOW_ENTITY:
				return HoverAction.SHOW_ENTITY;
			case SHOW_ACHIEVEMENT:
				return HoverAction.SHOW_ACHIEVEMENT;
			default:
				throw new IllegalArgumentException();
		}
	}

	public String getName()
	{
		return name().toLowerCase();
	}
}
