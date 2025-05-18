package io.github.sajge.desktop.profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import io.github.sajge.logger.Logger;

public class UserDialog extends JDialog {
    private static final Logger LOGGER = Logger.get(UserDialog.class);
    private JTextField displayNameField = new JTextField(20);
    private JTextField usernameField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);
    private JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
    private char defaultEchoChar;

    public UserDialog(Frame parent) {
        super(parent, "User Dialog", true);
        LOGGER.info("Initializing UserDialog");

        defaultEchoChar = passwordField.getEchoChar();

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addField(content, "Display Name:", displayNameField);
        addField(content, "Username:", usernameField);
        addPasswordField(content, "Password:", passwordField, showPasswordCheckBox);

        setContentPane(content);
        setMinimumSize(new Dimension(300, 200));
        pack();
        setLocationRelativeTo(parent);
    }

    private void addField(JPanel container, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(label);
        container.add(Box.createVerticalStrut(5));
        container.add(field);
        container.add(Box.createVerticalStrut(10));
    }

    private void addPasswordField(JPanel container, String labelText, JPasswordField field, JCheckBox checkbox) {
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkbox.setAlignmentX(Component.LEFT_ALIGNMENT);

        checkbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean selected = checkbox.isSelected();
                field.setEchoChar(selected ? (char)0 : defaultEchoChar);
                LOGGER.debug("Password visibility toggled: {}", selected);
            }
        });

        container.add(label);
        container.add(Box.createVerticalStrut(5));
        container.add(field);
        container.add(checkbox);
        container.add(Box.createVerticalStrut(10));
    }

    public static void main(String[] args) {
        LOGGER.info("Launching UserDialog");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UserDialog dialog = new UserDialog(null);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
            }
        });
    }

    public JTextField getDisplayName() {
        return displayNameField;
    }

    public void setDisplayName(JTextField displayNameField) {
        this.displayNameField = displayNameField;
    }

    public JTextField getUsername() {
        return usernameField;
    }

    public void setUsername(JTextField usernameField) {
        this.usernameField = usernameField;
    }

    public JPasswordField getPassword() {
        return passwordField;
    }

    public void setPassword(JPasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public JCheckBox getShowPasswordCheckBox() {
        return showPasswordCheckBox;
    }

    public void setShowPasswordCheckBox(JCheckBox showPasswordCheckBox) {
        this.showPasswordCheckBox = showPasswordCheckBox;
    }

    public char getDefaultEchoChar() {
        return defaultEchoChar;
    }

    public void setDefaultEchoChar(char defaultEchoChar) {
        this.defaultEchoChar = defaultEchoChar;
    }
}
