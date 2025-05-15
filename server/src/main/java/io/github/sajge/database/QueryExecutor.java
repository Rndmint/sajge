package io.github.sajge.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryExecutor {

    public List<Map<String, Object>> executeQuery(String sql, Object... args)
            throws SQLException, InterruptedException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            conn = DBConnectionPool.INSTANCE.get();
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                stmt.setObject(i + 1, args[i]);
            }

            rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                resultList.add(row);
            }

            return resultList;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) DBConnectionPool.INSTANCE.release(conn);
        }
    }

    public int executeUpdate(String sql, Object... args)
            throws SQLException, InterruptedException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnectionPool.INSTANCE.get();
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                stmt.setObject(i + 1, args[i]);
            }

            return stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) DBConnectionPool.INSTANCE.release(conn);
        }
    }
}
