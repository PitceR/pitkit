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

package pl.pitkour.pitkit.text.message.event.action;

import java.util.Objects;
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
		Objects.requireNonNull(action, "action must not be null");
		switch(action)
		{
			case RUN_COMMAND:
				return RUN_COMMAND;
			case SUGGEST_COMMAND:
				return SUGGEST_COMMAND;
			case OPEN_FILE:
				return OPEM_FILE;
			case OPEN_URL:
				return OPEN_URL;
			case CHANGE_PAGE:
				return CHANGE_PAGE;
			default:
				throw new IllegalArgumentException("missing click action type");
		}
	}

	public String getName()
	{
		return name().toLowerCase();
	}
}
