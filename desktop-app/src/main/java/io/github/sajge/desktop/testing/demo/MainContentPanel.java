package io.github.sajge.desktop.testing.demo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainContentPanel extends JPanel {
    private final CardLayout cardLayout = new CardLayout();

    public MainContentPanel() {
        super(new BorderLayout());
        JPanel cards = new JPanel(cardLayout);
        cards.setBorder(new EmptyBorder(20, 20, 20, 20));

        for (String name : Constants.VIEW_NAMES) {
            JPanel panel = new JPanel(new BorderLayout());
            JLabel lbl = new JLabel(name + " View");
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 18f));
            panel.add(lbl, BorderLayout.NORTH);
            cards.add(panel, name);
        }
        add(cards, BorderLayout.CENTER);
    }

    public void showView(String name) {
        cardLayout.show(((JPanel) getComponent(0)), name);
    }
}
