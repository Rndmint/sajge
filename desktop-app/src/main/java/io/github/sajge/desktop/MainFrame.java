package io.github.sajge.desktop;

import io.github.sajge.client.ClientService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final String CARD_AUTH = "AUTH";
    private static final String CARD_APP = "APP";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    private final ClientService service;
    private final AuthPanel authPanel;
    private final AppPanel appPanel;

    public MainFrame() {
        super("Sajge Swing Client");
        this.service = new ClientService();
        this.authPanel = new AuthPanel(this, service);
        this.appPanel = new AppPanel(this, service);

        cards.add(authPanel, CARD_AUTH);
        cards.add(appPanel, CARD_APP);

        setContentPane(cards);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        showAuth();
    }

    public void showApp(String username) {
        appPanel.getProfileTab().setUsername(username);
        cardLayout.show(cards, CARD_APP);
    }

    public void showAuth() {
        service.setToken("");
        authPanel.clearFields();
        appPanel.resetAllTabs();
        cards.add(authPanel, CARD_AUTH);
        cardLayout.show(cards, CARD_AUTH);
    }
}
