package com.clanevents;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ColumnCellRenderer implements TableCellRenderer
{
    private TableCellRenderer original;
    private Font font = null;
    private Color color = null;

    public ColumnCellRenderer(TableCellRenderer original)
    {
        this.original = original;
    }

    public void replaceRenderer(TableCellRenderer original)
    {
        this.original = original;
        font = null;
        color = null;
    }

    public void setFont(Font font)
    {
        this.font = font;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component comp = original.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (this.font != null)
        {
            comp.setFont(this.font);
        }

        if (this.color != null)
        {
            comp.setForeground(this.color);
        }

        return comp;
    }
}
