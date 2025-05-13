package io.github.sajge.swingui;

import javax.swing.*;

public class GTextArea extends GComponent<JTextArea, GTextArea> {
    public GTextArea() {
        super();
    }

    @Override
    protected JTextArea createComponent() {
        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    public GTextArea withRows(int rows) {
        component.setRows(rows);
        return this;
    }

    public GTextArea withColumns(int columns) {
        component.setColumns(columns);
        return this;
    }

    public GTextArea withScrollbars() {
        return this;
    }

    public String getText() {
        return component.getText();
    }

    public GTextArea withText(String text) {
        component.setText(text);
        return this;
    }
}
