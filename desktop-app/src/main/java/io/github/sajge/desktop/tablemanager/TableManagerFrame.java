package io.github.sajge.desktop.tablemanager;

import com.formdev.flatlaf.FlatLightLaf;
import io.github.sajge.logger.Logger;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.util.List;
import java.util.Arrays;
import java.util.regex.Pattern;

public class TableManagerFrame extends JFrame {
    private static final Logger log = Logger.get(TableManagerFrame.class);

    private final CustomTableModel model;
    private final TableController controller;
    private final JTable table;
    private final TableRowSorter<CustomTableModel> sorter;

    public TableManagerFrame(List<List<Object>> initialData) {
        super("Table Manager");
        log.info("Initializing TableManagerFrame with {} rows", initialData.size());
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            log.debug("FlatLaf look and feel set successfully");
        } catch (Exception ex) {
            log.error("Failed to set FlatLaf look and feel", ex);
        }

        model = new CustomTableModel(initialData);
        log.debug("CustomTableModel created with {} rows and {} columns", model.getRowCount(), model.getColumnCount());

        controller = new TableController(model, initialData.size() + 1);
        log.debug("TableController initialized with nextId={} ", controller.getNextId());

        table = new JTable(model);
        sorter = new TableRowSorter<>(model);

        setupSorter();
        setupUI();

        log.info("TableManagerFrame setup complete");
    }

    private void setupSorter() {
        log.debug("Configuring table sorter");
        sorter.addRowSorterListener(event -> log.debug("Table sorted, sort keys={}", sorter.getSortKeys()));
        table.setRowSorter(sorter);
        log.debug("TableRowSorter applied to JTable");
    }

    private void setupUI() {
        log.debug("Setting up UI components");
        JTextField searchField = new JTextField(15);
        searchField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update() {
                String filterText = searchField.getText();
                RowFilter<CustomTableModel, Integer> rowFilter = filterText.trim().isEmpty()
                        ? null
                        : RowFilter.regexFilter("(?i)" + Pattern.quote(filterText));
                sorter.setRowFilter(rowFilter);
                log.debug("Filter applied with text '{}', rowFilter={}", filterText, rowFilter);
            }
        });

        JButton addRowButton = new JButton("Add Row");
        addRowButton.addActionListener(event -> {
            log.info("Add Row button clicked, nextId={}", controller.getNextId());
            AddRowDialog.show(this, controller, model);
            log.debug("AddRowDialog completed, current row count={}", model.getRowCount());
        });

        JButton deleteRowsButton = new JButton("Delete Selected");
        deleteRowsButton.addActionListener(event -> {
            int[] selectedViewRows = table.getSelectedRows();
            log.info("Delete Selected button clicked, selected view rows={}", Arrays.toString(selectedViewRows));
            int[] modelRows = Arrays.stream(selectedViewRows)
                    .map(table::convertRowIndexToModel)
                    .sorted()
                    .toArray();
            model.deleteRows(modelRows);
            log.info("Deleted rows at model indices={}", Arrays.toString(modelRows));
        });

        JButton applyRemotelyButton = new JButton("Apply Remotely");
        applyRemotelyButton.addActionListener(event -> {
            log.info("Apply Remotely button clicked");
            List<List<Object>> newData = controller.applyOperations();
            log.info("Apply operations completed, table updated with {} rows", newData.size());
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Search:"));
        controlPanel.add(searchField);
        controlPanel.add(addRowButton);
        controlPanel.add(deleteRowsButton);
        controlPanel.add(applyRemotelyButton);

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        log.debug("UI frame dimensions set to 600x400 and centered");
    }

    public void setOperationListener(OperationListener listener) {
        controller.setOperationListener(listener);
        log.debug("OperationListener registered: {}", listener);
    }

    public void lockColumn(int columnIndex) {
        model.lockColumn(columnIndex);
        log.info("Column {} locked in TableManagerFrame", columnIndex);
    }

    public List<List<Object>> getInsertedRows() {
        log.debug("Retrieving inserted rows: count={}", model.getInsertedRows().size());
        return model.getInsertedRows();
    }

    public List<List<Object>> getDeletedRows() {
        log.debug("Retrieving deleted rows: count={}", model.getDeletedRows().size());
        return model.getDeletedRows();
    }

    public List<List<Object>> getUpdatedRows() {
        log.debug("Retrieving updated rows: count={}", model.getUpdatedRows().size());
        return model.getUpdatedRows();
    }

    public List<List<Object>> getAllData() {
        log.debug("Retrieving all data: rows={}", model.getAllData().size());
        return model.getAllData();
    }
}
