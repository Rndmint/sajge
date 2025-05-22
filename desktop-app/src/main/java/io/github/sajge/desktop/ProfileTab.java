package io.github.sajge.desktop;

import io.github.sajge.client.ClientService;

import javax.swing.*;
import java.awt.*;

public class ProfileTab extends JPanel {
    private final MainFrame parent;
    private final ClientService service;

    private final JLabel userLabel = new JLabel();
    private final JButton logoutBtn = new JButton("Logout");
    private final JButton deleteBtn = new JButton("Delete Account");

    public ProfileTab(MainFrame parent, ClientService service) {
        this.parent = parent;
        this.service = service;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Logged in as:"), gbc);
        gbc.gridx = 1;
        add(userLabel, gbc);

        JPanel btnPanel = new JPanel();
        btnPanel.add(logoutBtn);
        btnPanel.add(deleteBtn);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(btnPanel, gbc);

        logoutBtn.addActionListener(e -> doLogout());
        deleteBtn.addActionListener(e -> doDelete());
    }

    public void setUsername(String username) {
        userLabel.setText(username);
    }

    private void doLogout() {
        logoutBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        new SwingWorker<Boolean, Void>() {
            protected Boolean doInBackground() throws Exception {
                return service.logout();
            }

            protected void done() {
                logoutBtn.setEnabled(true);
                deleteBtn.setEnabled(true);
                try {
                    if (get()) {
                        parent.showAuth();
                    } else {
                        JOptionPane.showMessageDialog(ProfileTab.this,
                                "Logout failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ProfileTab.this,
                            "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void doDelete() {
        if (JOptionPane.showConfirmDialog(this,
                "Really delete your account?",
                "Confirm",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        logoutBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        new SwingWorker<Boolean, Void>() {
            protected Boolean doInBackground() throws Exception {
                return service.deleteAccount(userLabel.getText());
            }

            protected void done() {
                logoutBtn.setEnabled(true);
                deleteBtn.setEnabled(true);
                try {
                    if (get()) {
                        parent.showAuth();
                    } else {
                        JOptionPane.showMessageDialog(ProfileTab.this,
                                "Delete failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ProfileTab.this,
                            "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}

