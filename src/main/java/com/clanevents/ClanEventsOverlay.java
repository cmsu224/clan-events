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

import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
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
    @Inject
    private ClanEventsConfig config;

    @Inject
    private ClanEventsOverlay()
    {
        setPosition(OverlayPosition.TOP_CENTER);
        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Clan Events overlay"));
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        String text = config.eventPass() + " " + config.challengePass();
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
                ((LineComponent) elem.get(0)).setRight(localToGMT());
                ((LineComponent) elem.get(0)).setRightColor(timeColor);
            }

            panelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth(text) + 10, 0));
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
