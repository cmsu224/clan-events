/*
 * Copyright (c) 2018, Kruithne <kruithne@gmail.com>
 * Copyright (c) 2018, Psikoi <https://github.com/psikoi>
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

package com.elysiumevents;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

import com.elysiumevents.components.combobox.ComboBoxIconEntry;
import com.elysiumevents.components.combobox.ComboBoxIconListRenderer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;

@Slf4j
class ElysiumEventsPanel extends PluginPanel
{
    private final JPanel ssArea = new JPanel();
    private final JLabel ssText = new JLabel();
    private final GoogleSheet sheet = new GoogleSheet();
    final JComboBox<ComboBoxIconEntry> dropdown = new JComboBox<>();
    private String color1;
    private String color2;

    void init(ElysiumEventsConfig config, int index){
        // Google sheet API
        String data = "";
        sheet.setKey(config.apiKey());
        sheet.setSheetId(config.sheetId());

        //dropdown menu
        dropdown.setFocusable(false); // To prevent an annoying "focus paint" effect
        dropdown.setForeground(Color.WHITE);
        final ComboBoxIconListRenderer renderer = new ComboBoxIconListRenderer();
        dropdown.setRenderer(renderer);

        BufferedImage icon = ImageUtil.loadImageResource(getClass(), "home.png");
        dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Home", "home"));
        icon = ImageUtil.loadImageResource(getClass(), "sotw.png");
        dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Skill of the Week", "sotw"));
        icon = ImageUtil.loadImageResource(getClass(), "botw.png");
        dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Boss of the Week", "botw"));
        icon = ImageUtil.loadImageResource(getClass(), "event.png");
        dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Clan Event", "event"));
        icon = ImageUtil.loadImageResource(getClass(), "hof.png");
        dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Hall of Fame", "hof"));

        //Set the color
        color1 = "#"+Integer.toHexString(config.col1color().getRGB()).substring(2);
        color2 = "#"+Integer.toHexString(config.col2color().getRGB()).substring(2);

        dropdown.addItemListener(e ->
        {
            if (e.getStateChange() == ItemEvent.SELECTED)
            {
                final ComboBoxIconEntry source = (ComboBoxIconEntry) e.getItem();
                ssText.setText(getSheetDataFormatted(sheet, source.getData().toString()));
            }
        });

        dropdown.setSelectedIndex(index);

        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;

        this.add(dropdown, c);
        c.gridy++;

        //Bottom Textarea
        ComboBoxIconEntry selected = (ComboBoxIconEntry) dropdown.getSelectedItem();

        ssArea.removeAll();
        ssText.setText(getSheetDataFormatted(sheet, selected.getData().toString()));
        ssArea.add(ssText);
        this.add(ssArea, BorderLayout.NORTH);
        this.add(createRefreshButton(config), BorderLayout.NORTH);
    }

    private JButton createRefreshButton(ElysiumEventsConfig config)
    {
        final JButton label = new JButton("Refresh");
        label.setFocusable(false);
        label.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                label.setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                label.setBackground(ColorScheme.DARK_GRAY_COLOR);
            }

            @SneakyThrows
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    ComboBoxIconEntry selected = (ComboBoxIconEntry) dropdown.getSelectedItem();
                    ssText.setText(getSheetDataFormatted(sheet, selected.getData().toString()));
                }
            }
        });

        return label;
    }

    private String getSheetDataFormatted(GoogleSheet sheet, String field)
    {
        try
        {
            String data = "";
            java.util.List<java.util.List<Object>> values = sheet.getValues(field);

            if (values == null || values.isEmpty()) {
                System.out.println("No data found.");
            } else {
                data += "<html><table width=230>";
                for (List row : values) {
                    String val1 = "";
                    String val2 = "";

                    try
                    {
                        val1 = ""+row.get(0);
                    }catch(Exception e)
                    {
                        val1 = "";
                    }
                    try
                    {
                        val2 = ""+row.get(1);
                    }catch(Exception e)
                    {
                        val2 = "";
                    }

                    data += "<tr>";
                    data += "<td><font color='" + color1 + "'>";
                    data += val1;
                    data += "</font></td>";
                    data += "<td><font color='" + color2 + "'>";
                    data += val2;
                    data += "</font></td>";
                    data += "</tr>";
                }
                data += "</table></html>";
            }
            return data;
        }catch (Exception ioException)
        {
            return "Could not load google sheet data.";
        }
    }
}