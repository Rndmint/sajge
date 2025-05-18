package io.github.sajge.desktop.tablemanager;

import java.util.List;

public interface OperationListener {
    List<List<Object>> onOperations(
            List<List<Object>> inserted,
            List<List<Object>> deleted,
            List<List<Object>> updated);
}
