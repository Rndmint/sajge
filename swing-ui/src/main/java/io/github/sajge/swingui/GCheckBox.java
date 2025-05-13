package io.github.sajge.swingui;

import javax.swing.*;
import java.awt.event.ItemListener;

public class GCheckBox extends GComponent<JCheckBox, GCheckBox> {
    public GCheckBox() {
        super();
    }

    @Override
    protected JCheckBox createComponent() {
        return new JCheckBox();
    }

    public GCheckBox withChecked(boolean checked) {
        component.setSelected(checked);
        return this;
    }

    public GCheckBox onToggle(ItemListener listener) {
        component.addItemListener(listener);
        return this;
    }

    public boolean isChecked() {
        return component.isSelected();
    }

    @Override
    public GCheckBox withLanguageKey(String key) {
        super.withLanguageKey(key);
        component.putClientProperty("html.disable", Boolean.TRUE);
        return this;
    }
}
