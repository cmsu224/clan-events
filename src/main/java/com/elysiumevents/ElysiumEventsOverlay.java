package com.elysiumevents;

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

public class ElysiumEventsOverlay extends OverlayPanel
{
    private final Client client;
    private final ElysiumEventsPlugin plugin;
    @Inject
    private ElysiumEventsConfig config;

    @Inject
    private ElysiumEventsOverlay(Client client, ElysiumEventsPlugin plugin)
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
                    .color(Color.green)
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
