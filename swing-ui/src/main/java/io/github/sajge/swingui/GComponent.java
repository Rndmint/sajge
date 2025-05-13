package io.github.sajge.swingui;

import javax.swing.*;
import javax.swing.border.Border;

public abstract class GComponent
        <T extends JComponent, C extends GComponent<T, C>>
        implements LanguageSwitchable
{
    protected final T component;
    private String languageKey;
    private String tooltipText;

    protected GComponent() {
        this.component = createComponent();
        LangManager.INSTANCE.register(this);
    }

    protected abstract T createComponent();

    @SuppressWarnings("unchecked")
    protected final C self() {
        return (C) this;
    }

    public C withLanguageKey(String key) {
        this.languageKey = key;
        updateLanguage();
        return self();
    }

    public C withTooltip(String englishText) {
        this.tooltipText = englishText;
        component.setToolTipText(englishText);
        return self();
    }

    @Override
    public void updateLanguage() {
        if (languageKey != null) {
            updateComponentText(LangManager.INSTANCE.get(languageKey));
        }
    }

    protected void updateComponentText(String text) {
    }

    @Override
    public void cleanup() {
        LangManager.INSTANCE.unregister(this);
    }

    public C enabled(boolean state) {
        component.setEnabled(state);
        return self();
    }

    public C visible(boolean state) {
        component.setVisible(state);
        return self();
    }

    public C withBorder(Border border) {
        component.setBorder(border);
        return self();
    }

    protected final T getComponent() {
        throw new UnsupportedOperationException("Direct component access not allowed");
    }

    final T getWrappedComponent() {
        return component;
    }
}
