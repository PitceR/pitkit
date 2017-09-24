package pl.pitkour.pitkit.text.message.event;

import java.io.Serializable;
import pl.pitkour.pitkit.text.Text;
import pl.pitkour.pitkit.text.message.event.action.ClickAction;

public final class ClickEvent implements Serializable
{
	private ClickAction action;
	private Text value;

	public ClickEvent(ClickAction action, String value)
	{
		this(action, new Text(value));
	}

	public ClickEvent(ClickAction action, Text text)
	{
		this.action = action;
		this.value = text;
	}

	public ClickAction getAction()
	{
		return this.action;
	}

	public Text getValue()
	{
		return this.value;
	}
}
