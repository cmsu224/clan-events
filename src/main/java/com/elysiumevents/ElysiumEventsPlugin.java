package com.elysiumevents;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
		name = "Elysium Events",
		description = "For Elysium CC Events",
		tags = {"ely", "elysium", "cc", "hunt", "pass", "event"}
)
public class ElysiumEventsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ElysiumEventsConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ElysiumEventsOverlay overlay;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Provides
	ElysiumEventsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ElysiumEventsConfig.class);
	}
}
