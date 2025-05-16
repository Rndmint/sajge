package io.github.sajge.desktop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;

public class MainApp {
    private JFrame frame;
    private JSplitPane outerSplit;
    private JSplitPane innerSplit;
    private JPanel loginPanel;
    private JPanel crudPanel;
    private JPanel searchPanel;
    private JPanel tablePanel;

    public MainApp() {
        FlatLightLaf.install();

        frame = new JFrame();
        frame.setTitle("");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(usernameField, gbc);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        gbc.gridy = 1;
        loginPanel.add(passwordField, gbc);

        JButton loginButton = new JButton();
        loginButton.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 2;
        loginPanel.add(loginButton, gbc);

        crudPanel = new JPanel(new BorderLayout());

        searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(searchField);

        tablePanel = new JPanel(new BorderLayout());
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][] {},
                new String[] {"", ""}
        );
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton addButton = new JButton();
        JButton editButton = new JButton();
        JButton deleteButton = new JButton();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        innerSplit = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                searchPanel,
                tablePanel
        );
        innerSplit.setDividerLocation(250);
        crudPanel.add(innerSplit, BorderLayout.CENTER);

        outerSplit = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                loginPanel,
                crudPanel
        );
        outerSplit.setDividerLocation(200);
        frame.add(outerSplit);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}

