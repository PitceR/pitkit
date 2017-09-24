package pl.pitkour.pitkit.text.message.event;

import java.io.Serializable;
import pl.pitkour.pitkit.text.Text;
import pl.pitkour.pitkit.text.message.event.action.HoverAction;

public final class HoverEvent implements Serializable
{
	private HoverAction action;
	private Text value;

	public HoverEvent(HoverAction action, String value)
	{
		this(action, new Text(value));
	}

	public HoverEvent(HoverAction action, Text text)
	{
		this.action = action;
		this.value = text;
	}

	public HoverAction getAction()
	{
		return this.action;
	}

	public Text getValue()
	{
		return this.value;
	}
}
