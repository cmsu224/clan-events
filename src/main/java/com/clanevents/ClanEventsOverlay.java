package com.clanevents;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

public class ClanEventsOverlay extends OverlayPanel
{
    private final Client client;
    private final ClanEventsPlugin plugin;
    @Inject
    private ClanEventsConfig config;

    @Inject
    private ClanEventsOverlay(Client client, ClanEventsPlugin plugin)
    {
        super(plugin);
        setPosition(OverlayPosition.TOP_CENTER);
        this.client = client;
        this.plugin = plugin;
        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Event overlay"));
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        String text = config.eventPass() + " " + config.subPass();
        if (config.dtm())
        {
            text = text + " " + localToGMT();
        }

        if (!text.equals(" ") && !text.equals("  "))
        {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(text)
                    .color(config.passColor())
                    .build());

            panelComponent.setPreferredSize(new Dimension(
                    graphics.getFontMetrics().stringWidth(text) + 10,
                    0));
        }
        return super.render(graphics);
    }

    public static String localToGMT() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date) + " UTC";
    }
}
