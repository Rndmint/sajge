package io.github.sajge.swingui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class GTextField extends GComponent<JTextField, GTextField> {
    private ValueChangeListener<String> changeListener;

    public GTextField() {
        super();
    }

    @Override
    protected JTextField createComponent() {
        return new JTextField();
    }

    public GTextField withPlaceholder(String text) {
        component.putClientProperty("JTextField.placeholderText", text);
        return this;
    }

    public GTextField onTextChange(ValueChangeListener<String> listener) {
        this.changeListener = listener;
        return this;
    }

    public GTextField withColumns(int columns) {
        component.setColumns(columns);
        return this;
    }

    public String getText() {
        return component.getText();
    }

    public interface ValueChangeListener<T> {
        void onChange(T newValue);
    }
}
