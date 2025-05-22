package io.github.sajge.desktop.tables;

import io.github.sajge.desktop.tables.patterns.TableHandler;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ModelHandler<T> implements TableHandler<T> {
    private final String[] colomnNames;
    private final RowMapper<T> mapper;

    public ModelHandler(String[] columnNames, RowMapper<T> mapper) {
        this.colomnNames = columnNames;
        this.mapper = mapper;
    }

    @Override
    public boolean handle(TableHandlerContext<T> ctx) {
        List<T> list = (List<T>) ctx.getData();
        Object[][] rows = new Object[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            rows[i] = mapper.map(list.get(i));
        }
        DefaultTableModel model = new DefaultTableModel(rows, colomnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; //TODO probably?
            }
        };
        ctx.getTable().setModel(model);
        return false;
    }

    public interface RowMapper<T> {
        Object[] map(T item);
    }

}
