package io.github.sajge.desktop.tablemanager;

import io.github.sajge.logger.Logger;
import java.util.List;

public class TableController {
    private static final Logger log = Logger.get(TableController.class);

    private final CustomTableModel tableModel;
    private OperationListener operationListener;
    private int nextId;

    public TableController(CustomTableModel tableModel, int initialNextId) {
        this.tableModel = tableModel;
        this.nextId = initialNextId;
        log.info("TableController initialized with nextId={}", nextId);
    }

    public void setOperationListener(OperationListener listener) {
        this.operationListener = listener;
        log.debug("OperationListener set: {}", listener);
    }

    public int getNextId() {
        log.trace("getNextId -> {}", nextId);
        return nextId;
    }

    public void incrementNextId() {
        nextId++;
        log.debug("incrementNextId -> {}", nextId);
    }

    public List<List<Object>> applyOperations() {
        log.info("applyOperations triggered");

        List<List<Object>> insertedRows = tableModel.getInsertedRows();
        List<List<Object>> deletedRows = tableModel.getDeletedRows();
        List<List<Object>> updatedRows = tableModel.getUpdatedRows();

        log.debug("Preparing to apply operations - inserted={}, deleted={}, updated={}",
                insertedRows, deletedRows, updatedRows);

        if (operationListener == null) {
            log.warn("No OperationListener registered; returning current data");
            return tableModel.getAllData();
        }

        List<List<Object>> refreshedData = operationListener.onOperations(
                insertedRows, deletedRows, updatedRows
        );

        log.info("Received refreshed data with {} rows", refreshedData.size());

        tableModel.resetData(refreshedData);
        nextId = refreshedData.size() + 1;
        log.debug("nextId reset to {} after refresh", nextId);

        return refreshedData;
    }
}
