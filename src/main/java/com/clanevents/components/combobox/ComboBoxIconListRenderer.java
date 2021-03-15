package com.clanevents.components.combobox;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import lombok.Setter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.ComboBoxListRenderer;
import net.runelite.client.util.Text;

/**
 * Based off the {@link ComboBoxListRenderer} but supports icons and default text value
 */
public final class ComboBoxIconListRenderer extends JLabel implements ListCellRenderer
{
    @Setter
    private String defaultText = "Select an option...";

    @Override
    public Component getListCellRendererComponent(JList list, Object o, int index, boolean isSelected, boolean cellHasFocus)
    {
        if (isSelected)
        {
            setBackground(ColorScheme.DARK_GRAY_COLOR);
            setForeground(Color.WHITE);
        }
        else
        {
            setBackground(list.getBackground());
            setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        }

        setBorder(new EmptyBorder(5, 5, 5, 0));

        String text;
        setIcon(null);
        // If using setSelectedItem(null) or setSelectedIndex(-1) show default text until a selection is made
        if (index == -1 && o == null)
        {
            text = defaultText;
        }
        else if (o instanceof Enum)
        {
            text = Text.titleCase((Enum) o);
        }
        else if (o instanceof ComboBoxIconEntry)
        {
            final ComboBoxIconEntry e = (ComboBoxIconEntry) o;
            text = e.getText();
            setIcon(e.getIcon());
        }
        else
        {
            text = o.toString();
        }

        setText(text);

        return this;
    }
}