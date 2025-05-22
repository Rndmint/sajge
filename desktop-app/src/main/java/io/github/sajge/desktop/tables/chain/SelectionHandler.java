package io.github.sajge.desktop.tables.chain;


import io.github.sajge.desktop.tables.patterns.TableHandler;
import io.github.sajge.desktop.tables.TableHandlerContext;

public class SelectionHandler<T> implements TableHandler<T> {
    private final int mode;

    public SelectionHandler(int mode) {
        this.mode = mode;
    }

    @Override
    public boolean handle(TableHandlerContext<T> ctx) {
        ctx.getTable().setSelectionMode(mode);
        return false;
    }
}
