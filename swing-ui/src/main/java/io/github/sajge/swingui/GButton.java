package io.github.sajge.swingui;

import javax.swing.*;
import java.awt.event.ActionListener;

public class GButton extends GComponent<JButton, GButton> {
    public GButton() {
        super();
    }

    @Override
    protected JButton createComponent() {
        return new JButton();
    }

    public GButton onClick(ActionListener listener) {
        component.addActionListener(listener);
        return this;
    }

    public GButton withIcon(Icon icon) {
        component.setIcon(icon);
        return this;
    }
}
