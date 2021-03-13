package com.elysiumevents;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(ElysiumEventsPlugin.CONFIG_GROUP)
public interface ElysiumEventsConfig extends Config
{
	@ConfigSection(
			name = "Event Password",
			description = "Password info for clan event",
			position = 0
	)
	String eventPassSection = "Event Passphrase";

	@ConfigSection(
			name = "Google Sheets",
			description = "Google Sheet API Config",
			position = 1
	)
	String gSheetsSection = "Google Sheets API";

	@ConfigItem(
			position = 1,
			keyName = "eventPass",
			name = "Event Password",
			description = "Creates an overlay with the event password time",
			section = eventPassSection
	)
	default String eventPass()
	{
		return "Event Password";
	}
	@ConfigItem(
			position = 2,
			keyName = "dtm",
			name = "Date & Time",
			description = "Display the date and time",
			section = eventPassSection
	)
	default boolean dtm()
	{
		return true;
	}

	@ConfigItem(
			position = 3,
			keyName = "subPass",
			name = "Bounty/Challenge Password",
			description = "Display the sub passsword.",
			section = eventPassSection
	)
	default String subPass()
	{
		return "";
	}

	@ConfigItem(
			position = 4,
			keyName = "sheetId",
			name = "Google Sheet ID",
			description = "Id of the google sheet to read.",
			section = gSheetsSection
	)
	default String sheetId()
	{
		return "";
	}

	@ConfigItem(
			position = 5,
			keyName = "apiKey",
			name = "Google Sheet API Key",
			description = "Your auth to read the google sheet",
			section = gSheetsSection
	)
	default String apiKey()
	{
		return "";
	}
}