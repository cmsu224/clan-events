package com.clanevents;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;

import javax.inject.Inject;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.List;

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
        Color passColor = config.passColor();
        Color timeColor = config.timeColor();

        if(passColor.toString().equals(timeColor.toString())){
          passColor = Color.green;
          timeColor = Color.WHITE;
        }

        if (!text.equals(" ") && !text.equals("  ") && config.overlay())
        {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left(text)
                    .leftColor(passColor)
                    .build());

            if (config.dtm())
            {
                text = text + " " + localToGMT();
                List<LayoutableRenderableEntity> elem = panelComponent.getChildren();
                ((LineComponent) elem.get(0))
                        .setRight(localToGMT());
                ((LineComponent) elem.get(0))
                        .setRightColor(timeColor);
            }

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
