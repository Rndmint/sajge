package io.github.sajge.desktop.profile;

import io.github.sajge.logger.Logger;

import javax.swing.*;

public class ProfilePanel extends JPanel {
    private static final Logger logger = Logger.get(ProfilePanel.class);
    private final JLabel displayNameLabel;
    private final JLabel usernameLabel;

    public ProfilePanel(String displayName, String username) {
        logger.debug("Constructing ProfilePanel with displayName={} and username={}", displayName, username);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        displayNameLabel = new JLabel(displayName);
        add(displayNameLabel);
        usernameLabel = new JLabel(username);
        add(usernameLabel);
    }

    public void setDisplayName(String displayName) {
        logger.info("Updating displayName to={}", displayName);
        displayNameLabel.setText(displayName);
    }

    public void setUsername(String username) {
        logger.info("Updating username to={}", username);
        usernameLabel.setText(username);
    }

    public String getDisplayName() {
        return displayNameLabel.getText();
    }

    public String getUsername() {
        return usernameLabel.getText();
    }
}
