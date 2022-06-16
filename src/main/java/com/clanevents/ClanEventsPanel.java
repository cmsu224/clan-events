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

import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.*;
import javax.swing.*;

import com.clanevents.components.combobox.ComboBoxIconEntry;
import com.clanevents.components.combobox.ComboBoxIconListRenderer;
import com.clanevents.config.EntrySelect;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Semaphore;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;

@Slf4j
class ClanEventsPanel extends PluginPanel
{
    private final JPanel ssArea = new JPanel();
    private final GoogleSheet sheet = new GoogleSheet();
    private final Semaphore sem = new Semaphore(1);
    final JComboBox<ComboBoxIconEntry> dropdown = new JComboBox<>();
    private final ActionListener timertask = event -> {
        Object obj;

        //Bottom Textarea
        ComboBoxIconEntry selected = (ComboBoxIconEntry) dropdown.getSelectedItem();

        ssArea.removeAll();

        try {
            if (selected != null) {
                obj = selected.getData();
                if (obj != null) {
                    getSheetDataFormatted(obj.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ssArea.revalidate();
        ssArea.repaint();
        ssArea.setVisible(true);
    };
    private final Timer timer = new Timer(0, timertask);

    void init(ClanEventsConfig config){

        System.out.println("Initializing clan events panel");

        //Timer task
        if (config.autoRefresh()) {
            timer.setRepeats(true);
            timer.setDelay(config.refreshPeriod() * 1000 * 60);
        }
        else {
            timer.setRepeats(false);
        }

        //Google sheet API
        sheet.setKey(config.apiKey());
        sheet.setSheetId(config.sheetId());
        sheet.setTimeout(config.requestTimeout());
        ssArea.setLayout(new BoxLayout(ssArea, BoxLayout.Y_AXIS));

        //Dropdown menu
        dropdown.setFocusable(false); // To prevent an annoying "focus paint" effect
        dropdown.setForeground(Color.WHITE);
        final ComboBoxIconListRenderer renderer = new ComboBoxIconListRenderer();
        dropdown.setRenderer(renderer);

        setPage(config.entry_1());
        setPage(config.entry_2());
        setPage(config.entry_3());
        setPage(config.entry_4());
        setPage(config.entry_5());
        setPage(config.entry_6());
        setPage(config.entry_7());

        dropdown.addItemListener(event ->
        {
            if (event.getStateChange() == ItemEvent.SELECTED)
            {
                ssArea.setVisible(false);
                timer.restart();
                System.out.println("State changing...");
            }
        });

        dropdown.setSelectedIndex(0);
        this.add(dropdown);

        //Refresh Button
        this.add(createRefreshButton(), BorderLayout.NORTH);

        this.add(ssArea, BorderLayout.NORTH);
    }

    private void setPage(EntrySelect page) {

        BufferedImage icon;

        switch(page) {
            case HOME:
                icon = ImageUtil.loadImageResource(getClass(), "home.png");
                dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Home", "home"));
                break;

            case EVENTS:
                icon = ImageUtil.loadImageResource(getClass(), "events.png");
                dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Clan Events", "events"));
                break;

            case SOTW:
                icon = ImageUtil.loadImageResource(getClass(), "sotw.png");
                dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Skill of the Week", "sotw"));
                break;

            case BOTW:
                icon = ImageUtil.loadImageResource(getClass(), "botw.png");
                dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Boss of the Week", "botw"));
                break;

            case HOF_OVERALL:
                icon = ImageUtil.loadImageResource(getClass(), "hof.png");
                dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Hall of Fame - Overall", "hof_overall"));
                break;

            case HOF_KC:
                icon = ImageUtil.loadImageResource(getClass(), "hof.png");
                dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Hall of Fame - KC", "hof_kc"));
                break;

            case HOF_PB:
                icon = ImageUtil.loadImageResource(getClass(), "hof.png");
                dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Hall of Fame - PB", "hof_pb"));
                break;

            default:
                break;
        }
    }

    private JButton createRefreshButton()
    {
        final JButton button = new JButton("Refresh");
        button.setFocusable(false);
        button.addMouseListener(new MouseAdapter()
        {
            @SneakyThrows
            @Override
            public void mousePressed(MouseEvent event)
            {
                if (event.getButton() == MouseEvent.BUTTON1)
                {
                    if (sem.tryAcquire())
                    {
                        System.out.println("Acquired semaphore");
                        ssArea.setVisible(false);
                        timer.restart();
                        System.out.println("Refreshing...");
                        SwingUtilities.invokeLater(() -> {
                            try {
                                Thread.sleep(200);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ssArea.setVisible(true);
                            sem.release();
                        });
                    }
                    else
                    {
                        System.out.println("No semaphore");
                    }
                }
            }
        });

        return button;
    }

    private void createLinkEvent(JButton button, String url)
    {
        final String link = url;
        button.addMouseListener(new MouseAdapter()
        {
            @SneakyThrows
            @Override
            public void mousePressed(MouseEvent event)
            {
                if (event.getButton() == MouseEvent.BUTTON1)
                {
                    try {
                        //Open the URL
                        URI myURI = new URI(link);
                        openWebpage(myURI);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createHideEvent(JButton button)
    {
        button.addMouseListener(new MouseAdapter()
        {
            @SneakyThrows
            @Override
            public void mousePressed(MouseEvent event)
            {
                if (event.getButton() == MouseEvent.BUTTON1)
                {
                    try {
                        for (int i = 0; i < ssArea.getComponentCount(); ++i) {
                            //Search for the button's panel
                            if (ssArea.getComponent(i) == event.getComponent().getParent()) {
                                //Get the child of the panel directly after the button's panel
                                Container c = (Container) ssArea.getComponent(i + 1);
                                if (!c.getComponent(0).getClass().isAssignableFrom(JButton.class)) {
                                    //Toggle whether the panel is invisible
                                    c.setVisible(!c.isVisible());
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getSheetDataFormatted(String field)
    {
        System.out.println("Checking sheet for data");
        List<List<Object>> values;

        try {
            values = GoogleSheet.getValues(field);

            if (values == null) {
                System.out.println("Failed to get data from sheet");
            } else if (values.isEmpty()) {
                System.out.println("No data found");
            } else {
                System.out.println("Data found");

                JPanel panel;
                JScrollPane scroll;
                JButton button;
                TableColumn tc;
                String val1;
                String val2;
                String[] str;
                int i;
                int j;
                List<ColumnCellRenderer> hr;
                List<ColumnCellRenderer> cr;
                List<String> names;
                String[][] rows = new String[values.size()][];
                int style;
                Color color;
                Dimension d;
                String newLine;
                boolean addNewline;
                boolean setInvisible = false;

                j = 0;
                for (List<Object> lst : values) {
                    //noinspection SuspiciousToArrayCall
                    rows[j++] = lst.toArray(new String[0]);
                }

                // Go through every row from the sheet
                for (j = 0; j < rows.length; ++j) {

                    try {
                        val1 = ("" + rows[j][0]).trim();
                    } catch (Exception e) {
                        val1 = "";
                    }

                    switch (val1) {
                        case "<column>":
                            //Create the panel and table
                            panel = new JPanel(new BorderLayout());
                            panel.setBorder(new EmptyBorder(0, 0, 3, 0));
                            DefaultTableModel model = new DefaultTableModel();
                            JTable table = new JTable(model);
                            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

                            //Add the initial column
                            model.addColumn(null);
                            hr = new ArrayList<>();
                            cr = new ArrayList<>();
                            hr.add(new ColumnCellRenderer(new DefaultTableCellRenderer()));
                            cr.add(new ColumnCellRenderer(new DefaultTableCellRenderer()));
                            names = new ArrayList<>();
                            names.add("");
                            int column_val_idx = 0;

                            //Go through the rest of this row's values
                            for (i = 1; i < rows[j].length; ++i) {
                                try {
                                    val2 = ("" + rows[j][i]).trim();
                                } catch (Exception e) {
                                    val2 = "";
                                }

                                //A new column is being added
                                if (Objects.equals(val2, "<column>")) {
                                    model.addColumn(null);
                                    hr.add(new ColumnCellRenderer(new DefaultTableCellRenderer()));
                                    cr.add(new ColumnCellRenderer(new DefaultTableCellRenderer()));
                                    names.add("");
                                    column_val_idx = 0;
                                } else {
                                    try {
                                        switch (column_val_idx) {
                                            case 0:
                                                //The table column header's string
                                                names.set(model.getColumnCount() - 1, val2);
                                                break;

                                            case 1:
                                                //The table column's max width
                                                hr.get(model.getColumnCount() - 1).setMax(Integer.parseInt(val2));
                                                cr.get(model.getColumnCount() - 1).setMax(Integer.parseInt(val2));

                                            case 2:
                                                //The table column header's font
                                                val2 = val2.replaceAll(" +", " ");
                                                val2 = val2.replaceAll(", ", ",").toLowerCase();
                                                str = val2.split(",");

                                                switch(str[1]) {
                                                    case "bold":
                                                        style = Font.BOLD;
                                                        break;

                                                    case "italic":
                                                        style = Font.ITALIC;
                                                        break;

                                                    default:
                                                        style = Font.PLAIN;
                                                        break;
                                                }
                                                hr.get(model.getColumnCount() - 1).setFont(new Font(str[0], style, Integer.parseInt(str[2])));
                                                break;

                                            case 3:
                                                //The table column header's font color
                                                color = (Color) Color.class.getField(val2).get(null);
                                                hr.get(model.getColumnCount() - 1).setColor(color);
                                                break;

                                            case 4:
                                                //The table column cells' font
                                                val2 = val2.replaceAll(" +", " ");
                                                val2 = val2.replaceAll(", ", ",").toLowerCase();
                                                str = val2.split(",");

                                                switch(str[1]) {
                                                    case "bold":
                                                        style = Font.BOLD;
                                                        break;

                                                    case "italic":
                                                        style = Font.ITALIC;
                                                        break;

                                                    default:
                                                        style = Font.PLAIN;
                                                        break;
                                                }
                                                cr.get(model.getColumnCount() - 1).setFont(new Font(str[0], style, Integer.parseInt(str[2])));
                                                break;

                                            case 5:
                                                //The table column cells' font color
                                                color = (Color) Color.class.getField(val2).get(null);
                                                cr.get(model.getColumnCount() - 1).setColor(color);
                                                break;

                                            default:
                                                break;
                                        }
                                    } catch (Exception e) {
                                        //Invalid value
                                    }
                                    ++column_val_idx;
                                }
                            }

                            //Now that all of the columns have been added, make modifications to them
                            for (i = 0; i < model.getColumnCount(); ++i)
                            {
                                tc = table.getColumnModel().getColumn(i);
                                tc.setPreferredWidth(0);
                                tc.setHeaderValue(names.get(i));
                                tc.setHeaderRenderer(hr.get(i));
                                tc.setCellRenderer(cr.get(i));
                            }

                            ++j;
                            //Add the following rows of data to the table
                            for (; j < rows.length; ++j) {
                                try {
                                    val2 = rows[j][0];
                                } catch (Exception e) {
                                    val2 = "";
                                }

                                if (Objects.equals(val2.trim().toLowerCase(), "</column>")) {
                                    break;
                                } else {
                                    model.addRow(rows[j]);
                                }
                            }

                            //Set the preferred size so that a bunch of extra space isn't added below the table
                            d = new Dimension(table.getPreferredSize().width, table.getRowHeight() * table.getRowCount());
                            table.setPreferredScrollableViewportSize(d);
                            //Put it in a scrollpane so that the panel is the correct size
                            scroll = new JScrollPane(table);
                            scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                            scroll.removeMouseWheelListener(scroll.getMouseWheelListeners()[0]);
                            //Disable it to stop the annoying selection stuff
                            table.setEnabled(false);
                            scroll.setEnabled(false);
                            panel.add(scroll, BorderLayout.NORTH);
                            //Sets it invisible by default
                            if (setInvisible) {
                                panel.setVisible(false);
                                setInvisible = false;
                            }
                            ssArea.add(panel);
                            break;

                        case "<button>":
                            //Create the panel and button html text area
                            panel = new JPanel(new BorderLayout());
                            panel.setBorder(new EmptyBorder(0, 0, 3, 0));
                            button = new JButton("<html>");
                            button.setBorder(new EmptyBorder(3, 3, 3, 3));
                            button.setFocusable(false);

                            //Go through the rest of this row's values
                            for (i = 1; i < rows[j].length; ++i)
                            {
                                try {
                                    val2 = rows[j][i].trim();
                                } catch (Exception e) {
                                    val2 = "";
                                }

                                try {
                                    switch (i - 1) {
                                        case 0:
                                            //The text's alignment
                                            val2 = val2.toLowerCase();

                                            switch(val2) {
                                                case "right":
                                                    style = SwingConstants.RIGHT;
                                                    break;

                                                case "center":
                                                    style = SwingConstants.CENTER;
                                                    break;

                                                default:
                                                    style = SwingConstants.LEFT;
                                                    break;
                                            }
                                            button.setHorizontalAlignment(style);
                                            break;

                                        case 1:
                                            //The button's action
                                            val2 = val2.toLowerCase();

                                            switch(val2) {
                                                case "hide":
                                                    createHideEvent(button);
                                                    setInvisible = true;
                                                    break;

                                                case "show":
                                                    createHideEvent(button);
                                                    break;

                                                default:
                                                    if (!val2.isEmpty()) {
                                                        createLinkEvent(button, val2);
                                                    }
                                                    break;
                                            }
                                            break;

                                        default:
                                            break;
                                    }
                                } catch (Exception e) {
                                    // Invalid value
                                }
                            }

                            ++j;
                            newLine = "";
                            addNewline = false;
                            //Add values to the button html text area, where columns are concatenated with spaces between them and rows start on new lines
                            for (; j < rows.length; ++j) {

                                try {
                                    val2 = String.join(" ", rows[j]);
                                } catch (Exception e) {
                                    val2 = "";
                                }

                                if (Objects.equals(val2.trim().toLowerCase(), "</button>")) {
                                    button.setText(button.getText() + "</html>");
                                    break;
                                } else {
                                    button.setText(button.getText() + newLine + val2);
                                }

                                if (!addNewline) {
                                    newLine = "<br>";
                                    addNewline = true;
                                }
                            }

                            panel.add(button, BorderLayout.NORTH);
                            ssArea.add(panel);
                            break;

                        case "<text>":
                            //Create the panel and text area
                            panel = new JPanel(new BorderLayout());
                            panel.setBorder(new EmptyBorder(0, 0, 3, 0));
                            JTextArea text = new JTextArea();
                            text.setBorder(new EmptyBorder(3, 3, 4, 4));
                            text.setLayout(new BorderLayout());
                            //Enable text wrapping
                            text.setLineWrap(true);
                            text.setWrapStyleWord(true);

                            //Go through the rest of this row's values
                            for (i = 1; i < rows[j].length; ++i)
                            {
                                try {
                                    val2 = rows[j][i].trim();
                                } catch (Exception e) {
                                    val2 = "";
                                }

                                try {
                                    switch (i - 1) {
                                        case 0:
                                            //The text's font
                                            val2 = val2.replaceAll(" +", " ");
                                            val2 = val2.replaceAll(", ", ",").toLowerCase();
                                            str = val2.split(",");

                                            switch(str[1]) {
                                                case "bold":
                                                    style = Font.BOLD;
                                                    break;

                                                case "italic":
                                                    style = Font.ITALIC;
                                                    break;

                                                default:
                                                    style = Font.PLAIN;
                                                    break;
                                            }
                                            text.setFont(new Font(str[0], style, Integer.parseInt(str[2])));
                                            break;

                                        case 1:
                                            //The text's font color
                                            color = (Color) Color.class.getField(val2).get(null);
                                            text.setForeground(color);
                                            text.setDisabledTextColor(color);
                                            break;

                                        default:
                                            break;
                                    }
                                } catch (Exception e) {
                                    // Invalid value
                                }
                            }

                            ++j;
                            newLine = "";
                            addNewline = false;
                            //Add values to the text area, where columns are concatenated with spaces between them and rows start on new lines
                            for (; j < rows.length; ++j) {

                                try {
                                    val2 = String.join(" ", rows[j]);
                                } catch (Exception e) {
                                    val2 = "";
                                }

                                if (Objects.equals(val2.trim().toLowerCase(), "</text>")) {
                                    break;
                                } else {
                                    text.append(newLine + val2);
                                }

                                if (!addNewline) {
                                    newLine = System.lineSeparator();
                                    addNewline = true;
                                }
                            }

                            //Put it in a scrollpane so that the panel is the correct size
                            scroll = new JScrollPane(text);
                            scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                            scroll.removeMouseWheelListener(scroll.getMouseWheelListeners()[0]);
                            scroll.setBorder(new EmptyBorder(0, 0, -1, -1));
                            //Disable it to stop the annoying selection stuff
                            text.setEnabled(false);
                            panel.add(scroll, BorderLayout.NORTH);
                            //Sets it invisible by default
                            if (setInvisible) {
                                panel.setVisible(false);
                                setInvisible = false;
                            }
                            ssArea.add(panel);
                            break;

                        case "<html>":
                            ++j;
                        default:
                            //Create the panel and html text area by default
                            panel = new JPanel(new BorderLayout());
                            panel.setBorder(new EmptyBorder(0, 0, 3, 0));
                            JLabel label = new JLabel("<html>");
                            label.setBorder(new EmptyBorder(3, 3, 3, 3));

                            newLine = "";
                            addNewline = false;
                            //Add values to the html text area, where columns are concatenated with spaces between them and rows start on new lines
                            for (; j < rows.length; ++j) {
                                try {
                                    val2 = String.join(" ", rows[j]);
                                } catch (Exception e) {
                                    val2 = "";
                                }

                                if (Objects.equals(val2.trim().toLowerCase(), "</html>")) {
                                    label.setText(label.getText() + val2);
                                    break;
                                } else {
                                    label.setText(label.getText() + newLine + val2);
                                }

                                if (!addNewline) {
                                    newLine = "<br>";
                                    addNewline = true;
                                }
                            }

                            panel.add(label, BorderLayout.NORTH);
                            //Sets it invisible by default
                            if (setInvisible) {
                                panel.setVisible(false);
                                setInvisible = false;
                            }
                            ssArea.add(panel);
                            break;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivate() {
        ssArea.setVisible(false);
        timer.start();
        System.out.println("Timer started");
    }

    @Override
    public void onDeactivate() {
        timer.stop();
        System.out.println("Timer stopped");
    }
}

