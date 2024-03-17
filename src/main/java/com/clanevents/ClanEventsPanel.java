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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.swing.*;

import com.clanevents.components.combobox.ComboBoxIconEntry;
import com.clanevents.components.combobox.ComboBoxIconListRenderer;
import com.clanevents.constants.EntrySelect;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;
import net.runelite.client.util.SwingUtil;

@Slf4j
class ClanEventsPanel extends PluginPanel implements PropertyChangeListener {
    private final JButton buttonArea = createRefreshButton();
    private final JPanel ssArea = new JPanel();
    private final Service service = new Service();
    private final GoogleSheet sheet = new GoogleSheet();

    private Boolean openedTab = false;
    final JComboBox<ComboBoxIconEntry> dropdown = new JComboBox<>();
    final ComboBoxIconListRenderer renderer = new ComboBoxIconListRenderer();
    private final ActionListener timertask = event -> {
        if (!openedTab) {
            service.refreshData();
        } else {
            openedTab = false;
        }
    };

    private final Timer timer = new Timer(0, timertask);

    public void init(ClanEventsConfig config) {
        if (config.apiKey().equals("") || config.sheetId().equals("")) {
            JTextArea text = new JTextArea();
            text.append("Enter a valid Google Sheet ID " + "\n" + "or API Key in the config.");
            this.add(text);
            return;
        }
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        //Timer task setup
        if (config.autoRefresh()) {
            timer.setRepeats(true);
            timer.setDelay(config.refreshPeriod() * 1000 * 60);
        } else {
            timer.setRepeats(false);
        }

        //Disable to panel's scrollpane to remove its up and down arrow scrolling
        this.getScrollPane().setEnabled(false);
        //Disable tab as a traversal key
        this.setFocusTraversalKeysEnabled(false);

        //Add a mouselistener for getting focus so that keybindings work
        this.getParent().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocus();
            }
        });

        //Google sheet setup
        sheet.setKey(config.apiKey());
        sheet.setSheetId(config.sheetId());

        //Remove the annoying "focus paint" effect on the dropdown
        dropdown.setFocusable(false);

        //Dropdown setup
        dropdown.setForeground(Color.WHITE);
        dropdown.setRenderer(renderer);

        //Remove the item listener before removing all items
        if (dropdown.getItemListeners().length > 0) {
            dropdown.removeItemListener(dropdown.getItemListeners()[0]);
        }
        dropdown.removeAllItems();

        //Dropdown entries
        setEntry(config.entry_1());
        setEntry(config.entry_2());
        setEntry(config.entry_3());
        setEntry(config.entry_4());
        setEntry(config.entry_5());
        setEntry(config.entry_6());
        setEntry(config.entry_7());

        //Select the first entry
        dropdown.setSelectedIndex(0);

        //Dropdown selection listener
        dropdown.addItemListener(event ->
        {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                drawDataPanel();
            }
        });

        //Add dropdown to the panel
        this.add(dropdown);

        //Add refresh button to the panel
        this.add(buttonArea, BorderLayout.NORTH);

        //Keybinding inputs
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), KeyName.KN_1);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0), KeyName.KN_1);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), KeyName.KN_2);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0), KeyName.KN_2);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), KeyName.KN_3);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0), KeyName.KN_3);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0), KeyName.KN_4);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0), KeyName.KN_4);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_5, 0), KeyName.KN_5);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5, 0), KeyName.KN_5);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_6, 0), KeyName.KN_6);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0), KeyName.KN_6);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_7, 0), KeyName.KN_7);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7, 0), KeyName.KN_7);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), KeyName.KN_UP);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_UP, 0), KeyName.KN_UP);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), KeyName.KN_DOWN);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_DOWN, 0), KeyName.KN_DOWN);
        im.put(KeyStroke.getKeyStroke(config.entryKeybind().getKeyCode(), 0), KeyName.KN_KEYBIND);

        //Keybinding actions
        am.put(KeyName.KN_1, new KeyAction(KeyName.KN_1, config));
        am.put(KeyName.KN_2, new KeyAction(KeyName.KN_2, config));
        am.put(KeyName.KN_3, new KeyAction(KeyName.KN_3, config));
        am.put(KeyName.KN_4, new KeyAction(KeyName.KN_4, config));
        am.put(KeyName.KN_5, new KeyAction(KeyName.KN_5, config));
        am.put(KeyName.KN_6, new KeyAction(KeyName.KN_6, config));
        am.put(KeyName.KN_7, new KeyAction(KeyName.KN_7, config));
        am.put(KeyName.KN_UP, new KeyAction(KeyName.KN_UP, config));
        am.put(KeyName.KN_DOWN, new KeyAction(KeyName.KN_DOWN, config));
        am.put(KeyName.KN_KEYBIND, new KeyAction(KeyName.KN_KEYBIND, config));

        //Set the layout for the plugin's main panel
        ssArea.setLayout(new BoxLayout(ssArea, BoxLayout.Y_AXIS));

        //Add the plugin's main panel
        this.add(ssArea, BorderLayout.NORTH);
        service.addListener(this);
        if (service.state.equals(State.IDLE)) service.refreshData();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state") && evt.getNewValue() != evt.getOldValue()) {
            State currentState = (State) evt.getNewValue();
            updateButtonAsync(currentState);
            if (Objects.requireNonNull(currentState) == State.COMPLETED) {
                drawDataPanel();
            } else if (Objects.requireNonNull(currentState) == State.ERROR) {
                updateButtonAsync(State.ERROR);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateButtonAsync(State.COMPLETED);
            }
        }
    }

    private void drawDataPanel() {
        Optional.ofNullable((ComboBoxIconEntry) dropdown.getSelectedItem())
                .flatMap(ComboBoxIconEntry::getData)
                .ifPresent(this::updateUiAfterAPI);
    }

    private class KeyAction extends AbstractAction {

        KeyName key;
        ClanEventsConfig config;

        KeyAction(KeyName key, ClanEventsConfig config) {
            this.key = key;
            this.config = config;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int selected;
            switch (key) {
                case KN_1:
                case KN_2:
                case KN_3:
                case KN_4:
                case KN_5:
                case KN_6:
                case KN_7:
                    if (dropdown.isPopupVisible()) {
                        if (dropdown.getItemCount() >= key.getValue()) {
                            dropdown.setSelectedIndex(key.getValue() - 1);
                            dropdown.setPopupVisible(false);
                        }
                    }
                    break;
                case KN_UP:
                    if (dropdown.isPopupVisible()) {
                        selected = dropdown.getSelectedIndex();
                        if (selected == 0) {
                            dropdown.setSelectedIndex(dropdown.getItemCount() - 1);
                        } else {
                            dropdown.setSelectedIndex(selected - 1);
                        }
                        dropdown.setPopupVisible(false);
                    }
                    break;
                case KN_DOWN:
                    if (dropdown.isPopupVisible()) {
                        selected = dropdown.getSelectedIndex();
                        if (selected == (dropdown.getItemCount() - 1)) {
                            dropdown.setSelectedIndex(0);
                        } else {
                            dropdown.setSelectedIndex(selected + 1);
                        }
                        dropdown.setPopupVisible(false);
                    }
                    break;
                case KN_KEYBIND:
                    dropdown.setPopupVisible(!dropdown.isPopupVisible());
                    break;
            }
        }
    }

    public void setEntry(EntrySelect entry) {

        BufferedImage icon;

        switch (entry) {
            case HOME:
                icon = ImageUtil.loadImageResource(getClass(), "home.png");
                dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Home", Optional.of("home")));
                break;
            case HUB:
                icon = ImageUtil.loadImageResource(getClass(), "hub.png");
                dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Clan Hub", Optional.of("hub")));
                break;
            case SOTW:
                icon = ImageUtil.loadImageResource(getClass(), "sotw.png");
                dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Skill of the Week", Optional.of("sotw")));
                break;
            case BOTW:
                icon = ImageUtil.loadImageResource(getClass(), "botw.png");
                dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Boss of the Week", Optional.of("botw")));
                break;
            case HOF_OVERALL:
                icon = ImageUtil.loadImageResource(getClass(), "hof.png");
                dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Hall of Fame - Overall", Optional.of("hof_overall")));
                break;
            case HOF_KC:
                icon = ImageUtil.loadImageResource(getClass(), "hof.png");
                dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Hall of Fame - KC", Optional.of("hof_kc")));
                break;
            case HOF_PB:
                icon = ImageUtil.loadImageResource(getClass(), "hof.png");
                dropdown.addItem(new ComboBoxIconEntry(new ImageIcon(icon), " Hall of Fame - PB", Optional.of("hof_pb")));
                break;
        }
    }

    private JButton createRefreshButton() {
        final JButton button = new JButton("Refresh");
        button.setFocusable(false);

        button.addActionListener(e -> {
            if (service.state != State.LOADING) {
                service.refreshData();
            }
        });

        return button;
    }

    private void updateButtonAsync(State state) {
        SwingUtilities.invokeLater(() -> {
            buttonArea.setVisible(false);
            switch (state) {
                case LOADING:
                    buttonArea.setEnabled(false);
                    buttonArea.setText("Loading data...");
                    break;
                case ERROR:
                    buttonArea.setEnabled(false);
                    buttonArea.setText("<html><center><font color = red>Error fetching data!<br>Please wait a minute before trying again.</font></center></html>");
                    break;
                default:
                    buttonArea.setEnabled(true);
                    buttonArea.setText("Refresh");
                    break;
            }
            buttonArea.revalidate();
            buttonArea.repaint();
            buttonArea.setVisible(true);
        });
    }

    private static void createLinkEvent(JButton button, String url) {
        final String link = url;
        button.addMouseListener(new MouseAdapter() {
            @SneakyThrows
            @Override
            public void mousePressed(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    try {
                        //Open the URL
                        LinkBrowser.browse(new URI(link).toString());
                    } catch (NullPointerException | URISyntaxException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        });
    }

    private void createHideEvent(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @SneakyThrows
            @Override
            public void mousePressed(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void addTableMouseEvents(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            @SneakyThrows
            @Override
            public void mouseEntered(MouseEvent e) {
                java.awt.Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                int col = table.columnAtPoint(p);
                Object tooltip = table.getValueAt(row, col);
                table.setToolTipText(tooltip == null ? " " : tooltip.toString());
            }

            @SneakyThrows
            @Override
            public void mouseExited(MouseEvent e) {
                table.setToolTipText(null);
            }
        });
    }

    private void addParentMouseEvents(Component comp) {
        comp.addMouseListener(new MouseAdapter() {
            @SneakyThrows
            @Override
            public void mousePressed(MouseEvent e) {
                ssArea.getParent().getParent().dispatchEvent(e);
            }
        });
    }

    public void updateUiAfterAPI(String header) {
        service.getSheet(header).ifPresent(this::updatePanelAsync);
    }

    public void updatePanelAsync(List<List<Object>> values) {
        SwingUtilities.invokeLater(() -> formatPanelData(values));
    }

    private void formatPanelData(List<List<Object>> values) {
        ssArea.setVisible(false);
        SwingUtil.fastRemoveAll(ssArea);
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
                    addTableMouseEvents(table);
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

                                        switch (str[1]) {
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

                                        switch (str[1]) {
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

                    //Now that all the columns have been added, make modifications to them
                    for (i = 0; i < model.getColumnCount(); ++i) {
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
                    addParentMouseEvents(table);
                    addParentMouseEvents(table.getTableHeader());
                    //Disable it to stop the annoying selection stuff
                    table.setEnabled(false);
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
                    addParentMouseEvents(button);

                    //Go through the rest of this row's values
                    for (i = 1; i < rows[j].length; ++i) {
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

                                    switch (val2) {
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
                                    switch (val2.toLowerCase()) {
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
                    for (i = 1; i < rows[j].length; ++i) {
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

                                    switch (str[1]) {
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
                    scroll.setBorder(new EmptyBorder(0, 0, -1, -1));
                    addParentMouseEvents(text);
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

        ssArea.revalidate();
        ssArea.repaint();
        ssArea.setVisible(true);
    }

    @Override
    public void onActivate() {
        openedTab = true;
        timer.start();
    }

    @Override
    public void onDeactivate() {
        timer.stop();
    }
}

