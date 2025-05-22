package io.github.sajge.desktop.tables.chain;

import io.github.sajge.desktop.tables.patterns.TableHandler;
import io.github.sajge.desktop.tables.TableHandlerContext;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class RowClickHandler<T> implements TableHandler<T> {
    private final Consumer<Integer> onRowClick;

    public RowClickHandler(Consumer<Integer> onRowClick) {
        this.onRowClick = onRowClick;
    }

    @Override
    public boolean handle(TableHandlerContext<T> ctx) {
        ctx.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = ctx.getTable().rowAtPoint(e.getPoint());
                if (row >= 0) {
                    onRowClick.accept(row);
                }
            }
        });
        return false;
    }

}
