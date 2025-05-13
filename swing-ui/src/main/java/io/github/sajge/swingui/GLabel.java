package io.github.sajge.swingui;

import javax.swing.*;

public class GLabel extends GComponent<JLabel, GLabel> {
    public GLabel() {
        super();
    }

    @Override
    protected JLabel createComponent() {
        return new JLabel();
    }

    public GLabel withIcon(Icon icon) {
        component.setIcon(icon);
        return this;
    }

    public GLabel withHorizontalAlignment(int alignment) {
        component.setHorizontalAlignment(alignment);
        return this;
    }
}

