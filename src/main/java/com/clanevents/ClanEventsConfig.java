package com.clanevents;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup(ClanEventsPlugin.CONFIG_GROUP)
public interface ClanEventsConfig extends Config
{
	int TIMEOUT_MIN = 1;
	int TIMEOUT_MAX = 10;
	int REFRESH_PERIOD_MIN = 5;
	int REFRESH_PERIOD_MAX = 1440;

	@ConfigSection(
			name = "Overlay",
			description = "Overlay configuration.",
			position = 0
	)
	String overlaySection = "Event Passphrase";

	@ConfigSection(
			name = "Google Sheets",
			description = "Google Sheets configuration",
			position = 1
	)
	String gSheetsSection = "Google Sheets API";

	@ConfigItem(
			position = 1,
			keyName = "overlay",
			name = "Display Overlay",
			description = "Displays the overlay on your game screen.",
			section = overlaySection
	)
	default boolean overlay()
	{
		return false;
	}

	@ConfigItem(
			position = 2,
			keyName = "dtm",
			name = "Date & Time",
			description = "Adds the date and time to the overlay.",
			section = overlaySection
	)
	default boolean dtm()
	{
		return true;
	}

	@ConfigItem(
			position = 3,
			keyName = "eventPass",
			name = "Event Password:",
			description = "Adds the event password to the overlay.",
			section = overlaySection
	)
	default String eventPass()
	{
		return "";
	}

	@ConfigItem(
			position = 4,
			keyName = "challengePass",
			name = "Challenge Password:",
			description = "Adds the challenge password to the overlay.",
			section = overlaySection
	)
	default String challengePass()
	{
		return "";
	}

	@ConfigItem(
			position = 5,
			keyName = "disclaimer",
			name = "Colors below must be different",
			description = "The Password Color and the Date & Time Color must be different.",
			section = overlaySection
	)
	default void disclaimer() {}

	@ConfigItem(
			position = 6,
			keyName = "passColor",
			name = "Password Color",
			description = "The color of the Event Password and Challenge Password.",
			section = overlaySection
	)
	default Color passColor()
	{
		return Color.GREEN;
	}

	@ConfigItem(
			position = 7,
			keyName = "timeColor",
			name = "Date & Time Color",
			description = "The color of the Date & Time.",
			section = overlaySection
	)
	default Color timeColor()
	{
		return Color.WHITE;
	}

	@ConfigItem(
			position = 1,
			keyName = "sheetId",
			name = "Google Sheet ID:",
			description = "ID of the Google Sheet to read.",
			section = gSheetsSection
	)
	default String sheetId() { return "1YMcXxSL3s1NEzsPVMMkPn7EdGNFKENiwqNyDKkJTO80"; }

	@ConfigItem(
			position = 2,
			keyName = "apiKey",
			name = "Google API Key:",
			description = "Key used to access the Google Sheet (ask your clan for one).",
			section = gSheetsSection
	)
	default String apiKey() { return ""; }

	@Range(
			min = TIMEOUT_MIN,
			max = TIMEOUT_MAX
	)
	@ConfigItem(
			position = 3,
			keyName = "requestTimeout",
			name = "Request Timeout (s)",
			description = "(1-10) The Google Sheet HTTP request timeout in seconds.",
			section = gSheetsSection
	)
	default int requestTimeout() { return 3; }

	@ConfigItem(
			position = 4,
			keyName = "autoRefresh",
			name = "Automatic Refresh",
			description = "Enables automatic refreshing of the Clan Events panel.",
			section = gSheetsSection
	)
	default boolean autoRefresh() { return false; }

	@Range(
			min = REFRESH_PERIOD_MIN,
			max = REFRESH_PERIOD_MAX
	)
	@ConfigItem(
			position = 5,
			keyName = "refreshPeriod",
			name = "Refresh Period (min)",
			description = "(5-1440) How often the Automatic Refresh should occur in minutes.",
			section = gSheetsSection
	)
	default int refreshPeriod() { return 10; }
}