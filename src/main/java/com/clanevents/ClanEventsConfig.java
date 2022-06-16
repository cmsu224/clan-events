/*
 * Copyright (c) 2022, cmsu224 <https://github.com/cmsu224>
 * Copyright (c) 2022, Brianmm94 <https://github.com/Brianmm94>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.clanevents;

import com.clanevents.config.EntrySelect;
import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup(ClanEventsPlugin.CONFIG_GROUP)
public interface ClanEventsConfig extends Config
{
	int TIMEOUT_MIN = 1;
	int TIMEOUT_MAX = 5;
	int REFRESH_PERIOD_MIN = 5;
	int REFRESH_PERIOD_MAX = 1440;

	@ConfigSection(
			name = "Overlay",
			description = "Overlay configuration.",
			position = 0
	)
	String overlaySection = "Overlay section";

	@ConfigSection(
			name = "Plugin Panel",
			description = "Plugin panel configuration",
			position = 1
	)
	String panelSection = "Plugin Panel section";

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
			section = panelSection
	)
	default String sheetId() { return "1YMcXxSL3s1NEzsPVMMkPn7EdGNFKENiwqNyDKkJTO80"; }

	@ConfigItem(
			position = 2,
			keyName = "apiKey",
			name = "Google API Key:",
			description = "Key used to access the Google Sheet (ask your clan for one).",
			section = panelSection
	)
	default String apiKey() { return ""; }

	@Range(
			min = TIMEOUT_MIN,
			max = TIMEOUT_MAX
	)
	@ConfigItem(
			position = 3,
			keyName = "requestTimeout",
			name = "Request Timeout",
			description = "(1-5) The Google Sheet HTTP request timeout in seconds.",
			section = panelSection
	)
	@Units(Units.SECONDS)
	default int requestTimeout() { return 1; }

	@ConfigItem(
			position = 4,
			keyName = "autoRefresh",
			name = "Automatic Refresh",
			description = "Enables automatic refreshing of the Clan Events panel.",
			section = panelSection
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
			section = panelSection
	)
	@Units(Units.MINUTES)
	default int refreshPeriod() { return 10; }

	@ConfigItem(
			position = 6,
			keyName = "entry_1",
			name = "Entry 1",
			description = "Selects what to show for entry 1 of the Clan Events panel.",
			section = panelSection
	)
	default EntrySelect entry_1() { return EntrySelect.HOME; }

	@ConfigItem(
			position = 7,
			keyName = "entry_2",
			name = "Entry 2",
			description = "Selects what to show for entry 2 of the Clan Events panel.",
			section = panelSection
	)
	default EntrySelect entry_2() { return EntrySelect.EVENTS; }

	@ConfigItem(
			position = 8,
			keyName = "entry_3",
			name = "Entry 3",
			description = "Selects what to show for entry 3 of the Clan Events panel.",
			section = panelSection
	)
	default EntrySelect entry_3() { return EntrySelect.SOTW; }

	@ConfigItem(
			position = 9,
			keyName = "entry_4",
			name = "Entry 4",
			description = "Selects what to show for entry 4 of the Clan Events panel.",
			section = panelSection
	)
	default EntrySelect entry_4() { return EntrySelect.BOTW; }

	@ConfigItem(
			position = 10,
			keyName = "entry_5",
			name = "Entry 5",
			description = "Selects what to show for entry 5 of the Clan Events panel.",
			section = panelSection
	)
	default EntrySelect entry_5() { return EntrySelect.HOF_OVERALL; }

	@ConfigItem(
			position = 11,
			keyName = "entry_6",
			name = "Entry 6",
			description = "Selects what to show for entry 6 of the Clan Events panel.",
			section = panelSection
	)
	default EntrySelect entry_6() { return EntrySelect.HOF_KC; }

	@ConfigItem(
			position = 12,
			keyName = "entry_7",
			name = "Entry 7",
			description = "Selects what to show for entry 7 of the Clan Events panel.",
			section = panelSection
	)
	default EntrySelect entry_7() { return EntrySelect.HOF_PB; }
}