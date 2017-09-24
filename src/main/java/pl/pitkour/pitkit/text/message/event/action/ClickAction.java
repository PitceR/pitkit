package pl.pitkour.pitkit.text.message.event.action;

import net.md_5.bungee.api.chat.ClickEvent.Action;

public enum ClickAction
{
	RUN_COMMAND,
	SUGGEST_COMMAND,
	OPEM_FILE,
	OPEN_URL,
	CHANGE_PAGE;

	public static ClickAction get(Action action)
	{
		switch(action)
		{
			case RUN_COMMAND:
				return ClickAction.RUN_COMMAND;
			case SUGGEST_COMMAND:
				return ClickAction.SUGGEST_COMMAND;
			case OPEN_FILE:
				return ClickAction.OPEM_FILE;
			case OPEN_URL:
				return ClickAction.OPEN_URL;
			case CHANGE_PAGE:
				return ClickAction.CHANGE_PAGE;
			default:
				throw new IllegalArgumentException();
		}
	}

	public String getName()
	{
		return name().toLowerCase();
	}
}
