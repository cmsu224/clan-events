package com.clanevents;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class ColumnCellRenderer implements TableCellRenderer
{
    private final TableCellRenderer original;
    private Font font = null;
    private Color color = null;
    private int max = 20;

    public ColumnCellRenderer(TableCellRenderer original)
    {
        this.original = original;
    }

    public void setFont(Font font) { this.font = font; }

    public void setColor(Color color) { this.color = color; }

    public void setMax(int max) { this.max = max; }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component comp = original.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (this.font != null) {
            TableColumn tc = table.getColumnModel().getColumn(column);
            tc.setPreferredWidth(this.max);
            comp.setFont(this.font);
        }

        if (this.color != null) {
            comp.setForeground(this.color);
        }

        return comp;
    }
}
