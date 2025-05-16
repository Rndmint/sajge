package io.github.sajge.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import io.github.sajge.logger.Logger;

public enum DBConnectionPool {
    INSTANCE;

    private final Logger logger = Logger.get(DBConnectionPool.class);
    private String host;
    private int port;
    private String database;
    private int socketTimeoutMs;
    private String user;
    private String password;
    private int poolSize;
    private BlockingQueue<Connection> pool;

    public synchronized void init(String host,
                                  int port,
                                  String database,
                                  int socketTimeoutMs,
                                  String user,
                                  String password,
                                  int poolSize) throws SQLException {
        this.host = host;
        this.port = port;
        this.database = database;
        this.socketTimeoutMs = socketTimeoutMs;
        this.user = user;
        this.password = password;
        this.poolSize = poolSize;

        this.pool = new ArrayBlockingQueue<>(poolSize);
        pool.clear();

        for (int i = 0; i < poolSize; i++) {
            pool.offer(createConnection());
        }
        logger.info("Initialized connection pool with {} connections", poolSize);
    }

    public Connection get() throws InterruptedException, SQLException {
        Connection conn = pool.take();
        if (!conn.isValid(2)) {
            logger.debug("Replacing invalid connection");
            conn.close();
            conn = createConnection();
        }
        return conn;
    }

    public void release(Connection conn) {
        if (conn != null) {
            try {
                if (conn.isClosed()) {
                    pool.offer(createConnection());
                } else {
                    pool.offer(conn);
                }
            } catch (SQLException e) {
                logger.error("Error checking connection status", e);
                try {
                    pool.offer(createConnection());
                } catch (SQLException ex) {
                    logger.error("Failed to create replacement connection", ex);
                }
            }
        }
    }

    private Connection createConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("hash", password);
        props.setProperty("password", password);
        props.setProperty("allowPublicKeyRetrieval", "true");
        props.setProperty("useSSL", "false");
        props.setProperty("serverTimezone", "UTC");
        props.setProperty("socketTimeout", String.valueOf(socketTimeoutMs));

        try {
            return DriverManager.getConnection(buildUrl(), props);
        } catch (SQLException e) {
            logger.error("Failed to create database connection", e);
            throw e;
        }
    }

    private String buildUrl() {
        return String.format("jdbc:mysql://%s:%d/%s", host, port, database);
    }

    public void shutdown() {
        pool.forEach(c -> {
            try {
                if (!c.isClosed()) {
                    c.close();
                }
            } catch (SQLException e) {
                logger.error("Error closing connection", e);
            }
        });
        pool.clear();
        logger.info("Connection pool shutdown complete");
    }
}


