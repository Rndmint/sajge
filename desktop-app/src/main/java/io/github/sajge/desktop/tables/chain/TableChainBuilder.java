package io.github.sajge.desktop.tables.chain;

import io.github.sajge.desktop.tables.patterns.TableHandler;

import java.util.ArrayList;
import java.util.List;

public class TableChainBuilder<T> {
    private final List<TableHandler<T>> handlers = new ArrayList<>();

    public TableChainBuilder<T> add(TableHandler<T> handler) {
        handlers.add(handler);
        return this;
    }

    public TableHandler<T> build() {
        return ctx -> {
            for (TableHandler<T> h : handlers) {
                if (h.handle(ctx)) return true;
            }
            return false;
        };
    }
}
