package io.github.sajge.desktop;

import io.github.sajge.client.ClientService;

import javax.swing.*;
import java.awt.*;

public class AuthPanel extends JPanel {
    private final MainFrame parent;
    private final ClientService service;

    private final JTextField userField = new JTextField(15);
    private final JPasswordField passField = new JPasswordField(15);
    private final JButton signupBtn = new JButton("Sign Up");
    private final JButton loginBtn = new JButton("Log In");

    public AuthPanel(MainFrame parent, ClientService service) {
        this.parent = parent;
        this.service = service;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(passField, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.add(signupBtn);
        btnPanel.add(loginBtn);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(btnPanel, gbc);

        signupBtn.addActionListener(e -> doSignup());
        loginBtn.addActionListener(e -> doLogin());
    }

    private void setButtonsEnabled(boolean on) {
        signupBtn.setEnabled(on);
        loginBtn.setEnabled(on);
    }

    private void doSignup() {
        String u = userField.getText().trim();
        String p = new String(passField.getPassword());
        setButtonsEnabled(false);
        new SwingWorker<Boolean, Void>() {
            protected Boolean doInBackground() throws Exception {
                return service.signup(u, p);
            }

            protected void done() {
                setButtonsEnabled(true);
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(AuthPanel.this,
                                "Signup successful—please log in.");
                    } else {
                        JOptionPane.showMessageDialog(AuthPanel.this,
                                "Signup failed (username may be taken)",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AuthPanel.this,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void doLogin() {
        String u = userField.getText().trim();
        String p = new String(passField.getPassword());
        setButtonsEnabled(false);
        new SwingWorker<Boolean, Void>() {
            protected Boolean doInBackground() throws Exception {
                return service.login(u, p);
            }

            protected void done() {
                setButtonsEnabled(true);
                try {
                    if (get()) {
                        parent.showApp(u);
                    } else {
                        JOptionPane.showMessageDialog(AuthPanel.this,
                                "Login failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AuthPanel.this,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    public void clearFields() {
        userField.setText("");
        passField.setText("");
    }

}
