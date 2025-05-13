package io.github.sajge.swingui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GPanel extends GComponent<JPanel, GPanel> {
    private final List<GComponent<?, ?>> children = new ArrayList<>();

    public GPanel() {
        super();
    }

    @Override
    protected JPanel createComponent() {
        return new JPanel();
    }

    public GPanel withLayout(LayoutManager manager) {
        component.setLayout(manager);
        return this;
    }

    public GPanel addComponent(GComponent<?, ?> child) {
        children.add(child);
        component.add(child.getWrappedComponent());
        return this;
    }

    public GPanel withBorder(String titleKey) {
        component.setBorder(BorderFactory.createTitledBorder(
                LangManager.INSTANCE.get(titleKey)
        ));
        return this;
    }

    public GPanel addScrollable(GTextArea area) {
        children.add(area);
        component.add(new JScrollPane(area.getWrappedComponent()));
        return this;
    }

    @Override
    public void cleanup() {
        children.forEach(GComponent::cleanup);
        super.cleanup();
    }

    public JPanel asJPanel() {
        return component;
    }
}

