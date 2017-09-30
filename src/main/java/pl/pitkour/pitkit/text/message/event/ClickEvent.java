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
