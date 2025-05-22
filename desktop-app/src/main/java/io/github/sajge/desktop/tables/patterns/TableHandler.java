package io.github.sajge.desktop.tables.patterns;

import io.github.sajge.desktop.tables.TableHandlerContext;

public interface TableHandler<T> {
    boolean handle(TableHandlerContext<T> ctx);
}
