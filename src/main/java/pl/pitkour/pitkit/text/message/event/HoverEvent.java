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
package pl.pitkour.pitkit.text.message.event;

import java.io.Serializable;
import java.util.Objects;
import pl.pitkour.pitkit.text.Text;
import pl.pitkour.pitkit.text.message.event.action.HoverAction;

public final class HoverEvent implements Serializable
{
	private HoverAction action;
	private Text value;

	public HoverEvent(HoverEvent event)
	{
		this(event.action, event.value);
	}

	public HoverEvent(HoverAction action, String value)
	{
		this(action, Text.of(value));
	}

	public HoverEvent(HoverAction action, Text text)
	{
		Objects.requireNonNull(action, "action must not be null");
		Objects.requireNonNull(text, "text must not be null");
		this.action = action;
		this.value = text;
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
		HoverEvent that = (HoverEvent)object;
		return this.action == that.action && this.value.equals(that.value);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.action, this.value);
	}

	@Override
	public String toString()
	{
		return "HoverEvent{" + "action=" + this.action + ", value=" + this.value + '}';
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
