package io.github.sajge.desktop;

import com.formdev.flatlaf.FlatLightLaf;
import io.github.sajge.desktop.profile.ProfilePanel;
import io.github.sajge.desktop.profile.UserDialog;
import io.github.sajge.desktop.tablemanager.CustomTableModel;
import io.github.sajge.desktop.tablemanager.SimpleDocumentListener;
import io.github.sajge.desktop.tablemanager.TableController;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class App extends JFrame {
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private static final Color TEXT_COLOR       = new Color(200, 200, 200);
    private static final Color PRIMARY_COLOR    = new Color(170, 0, 255);
    private static final Color CRITICAL_COLOR   = new Color(255, 50, 50);

    private final ProfilePanel profilePanel;
    private final CustomTableModel tableModel;
    private final TableController tableController;
    private final JTable table;
    private final TableRowSorter<CustomTableModel> sorter;

    public App(List<List<Object>> initialData) {
        super("Combined Swing App");

        FlatLightLaf.setup();
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("Label.foreground", TEXT_COLOR);
        UIManager.put("TextField.background", Color.DARK_GRAY);
        UIManager.put("TextField.foreground", TEXT_COLOR);
        UIManager.put("Button.background", PRIMARY_COLOR);
        UIManager.put("Button.foreground", TEXT_COLOR);
        UIManager.put("Button.select", CRITICAL_COLOR);

        getContentPane().setBackground(BACKGROUND_COLOR);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        profilePanel = new ProfilePanel("John Doe", "@jdoe");
        JButton editProfileBtn = new JButton("Edit Profile");
        editProfileBtn.setForeground(TEXT_COLOR);
        editProfileBtn.addActionListener(this::onEditProfile);
        profilePanel.add(Box.createVerticalStrut(10));
        profilePanel.add(editProfileBtn);

        tableModel = new CustomTableModel(initialData);
        tableController = new TableController(tableModel, initialData.size() + 1);
        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JPanel tablePanel = createTablePanel();

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BACKGROUND_COLOR);
        tabs.setForeground(TEXT_COLOR);
        tabs.addTab("Profile", profilePanel);
        tabs.addTab("Data Table", tablePanel);

        add(tabs);
        setVisible(true);
    }

    private void onEditProfile(ActionEvent e) {
        UserDialog dialog = new UserDialog(this);
        dialog.setVisible(true);
        String newName = String.valueOf(dialog.getDisplayName());
        String newUser = String.valueOf(dialog.getUsername());
        profilePanel.setDisplayName(newName);
        profilePanel.setUsername(newUser);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        JPanel controls = new JPanel();
        controls.setBackground(BACKGROUND_COLOR);

        JTextField searchField = new JTextField(15);
        searchField.setMaximumSize(new Dimension(200, 30));
        searchField.getDocument().addDocumentListener((SimpleDocumentListener) () -> {
            String text = searchField.getText();
            sorter.setRowFilter(text.trim().isEmpty()
                    ? null
                    : RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
        });

        JButton addRowBtn = new JButton("Add Row");
        addRowBtn.addActionListener(evt -> io.github.sajge.desktop.tablemanager.AddRowDialog.show(
                this, tableController, tableModel));

        JButton deleteRowBtn = new JButton("Delete Selected");
        deleteRowBtn.setBackground(CRITICAL_COLOR);
        deleteRowBtn.addActionListener(evt -> {
            int[] sel = table.getSelectedRows();
            int[] modelIdx = java.util.Arrays.stream(sel)
                    .map(table::convertRowIndexToModel)
                    .sorted()
                    .toArray();
            tableModel.deleteRows(modelIdx);
        });

        JButton applyBtn = new JButton("Apply Remotely");
        applyBtn.addActionListener(evt -> tableController.applyOperations());

        controls.add(new JLabel("Search:"));
        controls.add(searchField);
        controls.add(addRowBtn);
        controls.add(deleteRowBtn);
        controls.add(applyBtn);

        panel.add(controls, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        List<List<Object>> data = new ArrayList<>();
        SwingUtilities.invokeLater(() -> new App(data));
    }
}
