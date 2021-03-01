package com.elysiumevents;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("Event Password")
public interface ElysiumEventsConfig extends Config
{
	@ConfigItem(
			position = 1,
			keyName = "eventPass",
			name = "Event Password",
			description = "Creates an overlay with the event password time"
	)
	default String eventPass()
	{
		return "Jack Mehoff";
	}
	@ConfigItem(
			position = 2,
			keyName = "dtm",
			name = "Date & Time",
			description = "Display the date and time"
	)
	default boolean dtm()
	{
		return true;
	}

	@ConfigItem(
			position = 3,
			keyName = "subPass",
			name = "Bounty/Challenge Password",
			description = "Display the sub passsword."
	)
	default String subPass()
	{
		return "";
	}
}