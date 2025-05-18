package io.github.sajge.server.pool;

import io.github.sajge.logger.Logger;
import io.github.sajge.server.config.DbConfig;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ConnectionPool {
    private static ConnectionPool instance;
    private final BlockingDeque<Connection> connectionPool;
    private static final Logger log = Logger.get(ConnectionPool.class);
    private final String jdbcUrl;

    private ConnectionPool(DbConfig config) throws SQLException {
        this.jdbcUrl = String.format(
                "jdbc:mysql://%s:%d/%s?user=%s&password=%s&useSSL=false",
                config.getHost(),
                config.getPort(),
                config.getDatabaseName(),
                config.getUsername(),
                URLEncoder.encode(config.getPassword(), StandardCharsets.UTF_8)
        );
        connectionPool = new LinkedBlockingDeque<>(config.getMaxPoolSize());
        for (int i = 0; i < config.getMaxPoolSize(); i++) {
            connectionPool.add(createConnection());
        }
        log.info("Initialized {} connections", connectionPool.size());
    }

    public static synchronized ConnectionPool getInstance(DbConfig config) throws SQLException {
        if (instance == null) {
            instance = new ConnectionPool(config);
        }
        return instance;
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }

    public Connection acquire() {
        Connection conn = connectionPool.poll();
        log.debug("Acquired connection {}", conn);
        return conn;
    }

    public void release(Connection connection) {
        if (connection != null) {
            connectionPool.offer(connection);
            log.debug("Released connection {}", connection);
        }
    }

    public void terminate() {
        int closedCount = 0;
        while (!connectionPool.isEmpty()) {
            Connection conn = connectionPool.poll();
            if (conn != null) {
                try {
                    conn.close();
                    closedCount++;
                } catch (SQLException e) {
                    log.error("Error closing connection", e);
                }
            }
        }
        log.info("Terminated {} connections", closedCount);
    }
}
