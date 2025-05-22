package io.github.sajge.desktop.tables;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableHandlerContext<T> {
    private final JTable table;
    private final List<T> data;
    private final Object service;
    private final Map<String, ActionListener> controllers = new HashMap<>();

    public TableHandlerContext(JTable table, List<T> data, Object service) {
        this.table = table;
        this.data = data;
        this.service = service;
    }

    public JTable getTable() {
        return table;
    }

    public List<T> getData() {
        return data;
    }

    public Object getService() {
        return service;
    }

    public Map<String, ActionListener> getControllers() {
        return controllers;
    }
}
