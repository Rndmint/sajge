package io.github.sajge.desktop.tablemanager;

import io.github.sajge.logger.Logger;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final Logger log = Logger.get(Main.class);

    public static void main(String[] args) {
        List<List<Object>> sample = Arrays.asList(
                Arrays.asList(1, "Alice", "Engineering", 30),
                Arrays.asList(2, "Bob", "Sales", 25),
                Arrays.asList(3, "Charlie", "Marketing", 28),
                Arrays.asList(4, "Diana", "HR", 32),
                Arrays.asList(5, "Ethan", "Finance", 27),
                Arrays.asList(6, "Fiona", "Legal", 29),
                Arrays.asList(7, "George", "IT", 31),
                Arrays.asList(8, "Hannah", "Support", 26),
                Arrays.asList(9, "Ian", "Design", 24),
                Arrays.asList(10, "Jane", "R&D", 33)
        );
        log.info("Starting application with {} initial rows", sample.size());
        SwingUtilities.invokeLater(() -> {
            log.info("Launching TableManagerFrame");
            TableManagerFrame frame = new TableManagerFrame(sample);
            frame.setOperationListener((insertedRows, deletedRows, updatedRows) -> {
                log.info("OperationListener invoked: insert={}, delete={}, update={}",
                        insertedRows.size(), deletedRows.size(), updatedRows.size());
                List<List<Object>> currentData = frame.getAllData();
                log.info("Returning {} rows from OperationListener", currentData.size());
                return currentData;
            });
            frame.setVisible(true);
            log.info("TableManagerFrame visible to user");
        });
    }
}
