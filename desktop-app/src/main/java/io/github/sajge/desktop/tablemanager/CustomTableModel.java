package io.github.sajge.desktop.tablemanager;

import io.github.sajge.logger.Logger;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

public class CustomTableModel extends AbstractTableModel {
    private static final Logger log = Logger.get(CustomTableModel.class);

    private final List<List<Object>> tableData = new ArrayList<>();
    private int columnCount;
    private final List<List<Object>> insertedRows = new ArrayList<>();
    private final List<List<Object>> removedRows = new ArrayList<>();
    private final Map<Object, List<Object>> updatedRowsById = new LinkedHashMap<>();
    private final Set<Integer> lockedColumns = new HashSet<>();

    public CustomTableModel(List<List<Object>> initialData) {
        log.info("Initializing CustomTableModel with {} rows", initialData.size());
        resetData(initialData);
    }

    public void lockColumn(int columnIndex) {
        lockedColumns.add(columnIndex);
        log.info("Column {} locked", columnIndex);
    }

    @Override
    public int getRowCount() {
        int rowCount = tableData.size();
        log.trace("getRowCount -> {}", rowCount);
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        log.trace("getColumnCount -> {}", columnCount);
        return columnCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        log.trace("getValueAt [row={}, col={}]", rowIndex, columnIndex);
        List<Object> row = tableData.get(rowIndex);
        Object value = columnIndex < row.size() ? row.get(columnIndex) : null;
        log.trace("Value at [row={}, col={}] -> {}", rowIndex, columnIndex, value);
        return value;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean editable = columnIndex != 0 && !lockedColumns.contains(columnIndex);
        log.trace("isCellEditable [row={}, col={}] -> {}", rowIndex, columnIndex, editable);
        return editable;
    }

    @Override
    public void setValueAt(Object newValue, int rowIndex, int columnIndex) {
        if (newValue == null || newValue.toString().trim().isEmpty()) {
            log.warn("Rejected empty value for [row={}, col={}]", rowIndex, columnIndex);
            return;
        }
        log.info("setValueAt [row={}, col={}] -> {}", rowIndex, columnIndex, newValue);
        List<Object> row = tableData.get(rowIndex);
        Object oldValue = columnIndex < row.size() ? row.set(columnIndex, newValue) : null;
        if (columnIndex >= row.size()) {
            while (row.size() < columnIndex) row.add(null);
            row.add(newValue);
        }
        Object id = row.get(0);
        updatedRowsById.put(id, new ArrayList<>(row));
        log.debug("Updated row with id {}: {}", id, row);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void insertRow(List<Object> newRow) {
        log.info("insertRow -> {}", newRow);
        tableData.add(newRow);
        insertedRows.add(new ArrayList<>(newRow));
        int newRowIndex = tableData.size() - 1;
        log.debug("Inserted row at index {}", newRowIndex);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void deleteRows(int[] rowIndices) {
        if (rowIndices == null || rowIndices.length == 0) {
            log.warn("deleteRows called with no indices, skipping");
            return;
        }
        log.info("deleteRows -> indices={}", Arrays.toString(rowIndices));
        for (int i = rowIndices.length - 1; i >= 0; i--) {
            List<Object> removedRow = tableData.remove(rowIndices[i]);
            removedRows.add(new ArrayList<>(removedRow));
            log.debug("Removed row at index {}: {}", rowIndices[i], removedRow);
        }
        fireTableRowsDeleted(rowIndices[0], rowIndices[rowIndices.length - 1]);
    }

    public void resetData(List<List<Object>> newData) {
        log.info("resetData with {} rows", newData.size());
        tableData.clear();
        for (List<Object> row : newData) tableData.add(new ArrayList<>(row));
        columnCount = tableData.stream().mapToInt(List::size).max().orElse(0);
        insertedRows.clear();
        removedRows.clear();
        updatedRowsById.clear();
        log.debug("Data reset complete, columnCount={}" , columnCount);
        fireTableStructureChanged();
    }

    public List<List<Object>> getInsertedRows() {
        log.trace("getInsertedRows -> count={}", insertedRows.size());
        return Collections.unmodifiableList(insertedRows);
    }

    public List<List<Object>> getDeletedRows() {
        log.trace("getDeletedRows -> count={}", removedRows.size());
        return Collections.unmodifiableList(removedRows);
    }

    public List<List<Object>> getUpdatedRows() {
        log.trace("getUpdatedRows -> count={}", updatedRowsById.size());
        return Collections.unmodifiableList(new ArrayList<>(updatedRowsById.values()));
    }

    public List<List<Object>> getAllData() {
        log.trace("getAllData -> totalRows={}", tableData.size());
        List<List<Object>> copy = new ArrayList<>();
        for (List<Object> row : tableData) copy.add(new ArrayList<>(row));
        return copy;
    }
}
