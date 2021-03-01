package com.elysiumevents;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ElysiumEventsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ElysiumEventsPlugin.class);
		RuneLite.main(args);
	}
}