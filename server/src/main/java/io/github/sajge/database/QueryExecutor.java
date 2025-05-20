package io.github.sajge.database;

import io.github.sajge.logger.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class QueryExecutor {
    private static final Logger logger = Logger.get(QueryExecutor.class);

    public static class UpdateResult {
        private final int updateCount;
        private final List<Long> generatedKeys;

        public UpdateResult(int updateCount, List<Long> generatedKeys) {
            this.updateCount = updateCount;
            this.generatedKeys = generatedKeys;
        }

        public int getUpdateCount() {
            return updateCount;
        }

        public List<Long> getGeneratedKeys() {
            return generatedKeys;
        }
    }

    public List<Map<String, Object>> executeQuery(String sql, Object... args)
            throws SQLException, InterruptedException {
        logger.debug("executeQuery() called with sql={}, args={}", sql, Arrays.toString(args));
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            logger.debug("Acquiring connection from pool");
            conn = DBConnectionPool.INSTANCE.get();
            logger.debug("Connection acquired: {}", conn);

            stmt = conn.prepareStatement(sql);
            logger.trace("Prepared statement: {}", stmt);

            for (int i = 0; i < args.length; i++) {
                stmt.setObject(i + 1, args[i]);
                logger.trace("Set parameter {} to {}", i + 1, args[i]);
            }

            logger.info("Executing query: {}", sql);
            rs = stmt.executeQuery();
            logger.debug("Query executed, obtaining results");

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            logger.trace("ResultSet metadata - columnCount={}", columnCount);

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                    logger.trace("Row column {}: name={}, value={}", i, columnName, value);
                }
                resultList.add(row);
                logger.trace("Added row: {}", row);
            }

            logger.info("Query returned {} rows", resultList.size());
            return resultList;
        } finally {
            if (rs != null) {
                try {
                    logger.trace("Closing ResultSet");
                    rs.close();
                } catch (SQLException e) {
                    logger.error("Error closing ResultSet", e);
                }
            }
            if (stmt != null) {
                try {
                    logger.trace("Closing PreparedStatement");
                    stmt.close();
                } catch (SQLException e) {
                    logger.error("Error closing PreparedStatement", e);
                }
            }
            if (conn != null) {
                logger.debug("Releasing connection back to pool: {}", conn);
                DBConnectionPool.INSTANCE.release(conn);
            }
        }
    }

    public int executeUpdate(String sql, Object... args)
            throws SQLException, InterruptedException {
        logger.debug("executeUpdate() called with sql={}, args={}", sql, Arrays.toString(args));
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            logger.debug("Acquiring connection from pool");
            conn = DBConnectionPool.INSTANCE.get();
            logger.debug("Connection acquired: {}", conn);

            stmt = conn.prepareStatement(sql);
            logger.trace("Prepared statement: {}", stmt);

            for (int i = 0; i < args.length; i++) {
                stmt.setObject(i + 1, args[i]);
                logger.trace("Set parameter {} to {}", i + 1, args[i]);
            }

            logger.info("Executing update: {}", sql);
            int updateCount = stmt.executeUpdate();
            logger.info("Update affected {} rows", updateCount);
            return updateCount;
        } finally {
            if (stmt != null) {
                try {
                    logger.trace("Closing PreparedStatement");
                    stmt.close();
                } catch (SQLException e) {
                    logger.error("Error closing PreparedStatement", e);
                }
            }
            if (conn != null) {
                logger.debug("Releasing connection back to pool: {}", conn);
                DBConnectionPool.INSTANCE.release(conn);
            }
        }
    }

    public UpdateResult executeUpdateWithKeys(String sql, Object... args)
            throws SQLException, InterruptedException {
        logger.debug("executeUpdateWithKeys() called with sql={}, args={}", sql, Arrays.toString(args));
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            logger.debug("Acquiring connection from pool");
            conn = DBConnectionPool.INSTANCE.get();
            logger.debug("Connection acquired: {}", conn);

            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            logger.trace("Prepared statement (with RETURN_GENERATED_KEYS): {}", stmt);

            for (int i = 0; i < args.length; i++) {
                stmt.setObject(i + 1, args[i]);
                logger.trace("Set parameter {} to {}", i + 1, args[i]);
            }

            logger.info("Executing update: {}", sql);
            int updateCount = stmt.executeUpdate();
            logger.info("Update affected {} rows", updateCount);

            List<Long> keys = new ArrayList<>();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                while (rs.next()) {
                    long key = rs.getLong(1);
                    keys.add(key);
                    logger.trace("Generated key: {}", key);
                }
            }

            return new UpdateResult(updateCount, keys);
        } finally {
            if (stmt != null) {
                try {
                    logger.trace("Closing PreparedStatement");
                    stmt.close();
                } catch (SQLException e) {
                    logger.error("Error closing PreparedStatement", e);
                }
            }
            if (conn != null) {
                logger.debug("Releasing connection back to pool: {}", conn);
                DBConnectionPool.INSTANCE.release(conn);
            }
        }
    }
}
