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
import net.md_5.bungee.api.chat.HoverEvent.Action;

public enum HoverAction
{
	SHOW_TEXT,
	SHOW_ITEM,
	SHOW_ENTITY,
	SHOW_ACHIEVEMENT;

	public static HoverAction get(Action action)
	{
		Objects.requireNonNull(action, "action must not be null");
		switch(action)
		{
			case SHOW_TEXT:
				return SHOW_TEXT;
			case SHOW_ITEM:
				return SHOW_ITEM;
			case SHOW_ENTITY:
				return SHOW_ENTITY;
			case SHOW_ACHIEVEMENT:
				return SHOW_ACHIEVEMENT;
			default:
				throw new IllegalArgumentException("missing hover action type");
		}
	}

	public String getName()
	{
		return name().toLowerCase();
	}
}
