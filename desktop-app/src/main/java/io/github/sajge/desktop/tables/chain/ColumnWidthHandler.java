package io.github.sajge.desktop.tables.chain;

import io.github.sajge.desktop.tables.patterns.TableHandler;
import io.github.sajge.desktop.tables.TableHandlerContext;

import javax.swing.table.TableColumnModel;

public class ColumnWidthHandler<T> implements TableHandler<T> {
    private final int[] widths;

    public ColumnWidthHandler(int... widths) {
        this.widths = widths;
    }

    @Override
    public boolean handle(TableHandlerContext<T> ctx) {
        TableColumnModel cm = ctx.getTable().getColumnModel();
        for (int i = 0; i < widths.length && i < cm.getColumnCount(); i++) {
            cm.getColumn(i).setPreferredWidth(widths[i]);
        }
        return false;
    }
}
