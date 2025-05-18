package io.github.sajge.desktop.tablemanager;

import io.github.sajge.logger.Logger;

import javax.swing.*;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class AddRowDialog {
    private static final Logger log = Logger.get(AddRowDialog.class);

    public static void show(JFrame parent, TableController controller, CustomTableModel model) {
        log.info("Opening AddRowDialog");
        int columnCount = model.getColumnCount();
        JPanel inputPanel = new JPanel(new GridLayout(columnCount, 2));
        log.debug("Initialized input panel for {} columns", columnCount);

        JTextField[] textFields = new JTextField[columnCount];
        for (int colIndex = 0; colIndex < columnCount; colIndex++) {
            String labelText = (colIndex == 0) ? "ID" : "Column " + colIndex;
            inputPanel.add(new JLabel(labelText));
            JTextField textField = new JTextField();
            if (colIndex == 0) {
                String idValue = String.valueOf(controller.getNextId());
                textField.setText(idValue);
                textField.setEditable(false);
                log.debug("Pre-filled ID field with {}", idValue);
            }
            textFields[colIndex] = textField;
            inputPanel.add(textField);
        }
        log.trace("All input fields added to panel");

        int result = JOptionPane.showConfirmDialog(parent, inputPanel, "New Row", JOptionPane.OK_CANCEL_OPTION);
        log.info("AddRowDialog closed with option {}", result == JOptionPane.OK_OPTION ? "OK" : "CANCEL");

        if (result == JOptionPane.OK_OPTION) {
            List<Object> newRowData = new ArrayList<>();
            for (JTextField textField : textFields) {
                String value = textField.getText().trim();
                if (value.isEmpty()) {
                    log.warn("Empty value in new row; cancelling insertion");
                    JOptionPane.showMessageDialog(parent, "All fields must be non-empty", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                newRowData.add(value);
                log.trace("Collected field value: {}", value);
            }
            model.insertRow(newRowData);
            log.info("Inserted new row: {}", newRowData);
            controller.incrementNextId();
            log.debug("nextId incremented to {}", controller.getNextId());
        } else {
            log.debug("User cancelled AddRowDialog; no row inserted");
        }
    }
}
