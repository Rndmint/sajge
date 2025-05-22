package io.github.sajge.desktop.tables.chain;

import io.github.sajge.desktop.tables.patterns.TableHandler;
import io.github.sajge.desktop.tables.TableHandlerContext;

import javax.swing.table.TableCellRenderer;

public class RendererHandler<T> implements TableHandler<T> {
    private final int columnIndex;
    private final TableCellRenderer renderer;

    public RendererHandler(int columnIndex, TableCellRenderer renderer) {
        this.columnIndex = columnIndex;
        this.renderer = renderer;
    }

    @Override
    public boolean handle(TableHandlerContext<T> ctx) {
        ctx.getTable().getColumnModel()
                .getColumn(columnIndex)
                .setCellRenderer(renderer);
        return false;
    }
}
