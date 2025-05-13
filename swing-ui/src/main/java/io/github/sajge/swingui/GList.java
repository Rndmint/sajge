package io.github.sajge.swingui;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;

public class GList<E> extends GComponent<JList<E>, GList<E>> {
    private List<E> staticItems;

    public GList() {
        super();
    }

    @Override
    protected JList<E> createComponent() {
        return new JList<>();
    }

    @SuppressWarnings("unchecked")
    public GList<E> withItems(List<E> items) {
        this.staticItems = new ArrayList<>(items);
        component.setListData((E[]) items.toArray());
        return this;
    }

    public GList<E> withModel(ListModel<E> model) {
        component.setModel(model);
        return this;
    }

    public GList<E> withSelectionMode(int selectionMode) {
        component.setSelectionMode(selectionMode);
        return this;
    }

    public GList<E> onSelection(ListSelectionListener listener) {
        component.addListSelectionListener(listener);
        return this;
    }

    @Override
    public void updateLanguage() {
        if (staticItems != null) {
            withItems(staticItems);
        }
    }
}
