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

    private static final Logger logger = Logger.get(DBConnectionPool.class);

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
        logger.debug(
                "init() called with host={}, port={}, database={}, socketTimeoutMs={}, user={}, poolSize={}",
                host, port, database, socketTimeoutMs, user, poolSize);

        this.host = host;
        this.port = port;
        this.database = database;
        this.socketTimeoutMs = socketTimeoutMs;
        this.user = user;
        this.password = password;
        this.poolSize = poolSize;

        this.pool = new ArrayBlockingQueue<>(poolSize);
        pool.clear(); // I have no use for this, I thought I would need it at some point, but I don't, I'll keep it anyway who cares
        logger.trace("Cleared existing pool (if any)");

        for (int i = 0; i < poolSize; i++) {
            Connection conn = createConnection();
            pool.offer(conn);
            logger.debug("Added connection {} to pool", i + 1);
        }

        logger.info("Initialized connection pool with {} connections", poolSize);
    }

    public Connection get() throws InterruptedException, SQLException {
        logger.debug("get() called - waiting to take a connection from pool");
        Connection conn = pool.take();
        logger.debug("get() obtained connection: {}", conn);

        if (!conn.isValid(2)) {
            logger.warn("get() found invalid connection: {}", conn);
            conn.close();
            logger.debug("Closed invalid connection; creating replacement");
            conn = createConnection();
            logger.debug("Replacement connection created: {}", conn);
        } else {
            logger.trace("get() validated connection: {}", conn);
        }

        return conn;
    }

    public void release(Connection conn) {
        logger.debug("release() called with connection: {}", conn);
        if (conn != null) {
            try {
                if (conn.isClosed()) {
                    logger.warn("release() detected closed connection; creating replacement");
                    Connection replacement = createConnection();
                    pool.offer(replacement);
                    logger.debug("Offered replacement connection to pool: {}", replacement);
                } else {
                    pool.offer(conn);
                    logger.trace("Offered existing connection back to pool: {}", conn);
                }
            } catch (SQLException e) {
                logger.error("Error checking connection status", e);
                try {
                    Connection replacement = createConnection();
                    pool.offer(replacement);
                    logger.debug("Offered replacement connection after exception: {}",
                            replacement);
                } catch (SQLException ex) {
                    logger.error("Failed to create replacement connection", ex);
                }
            }
        } else {
            logger.warn("release() called with null connection; ignoring");
        }
    }

    private Connection createConnection() throws SQLException {
        String url = buildUrl();
        logger.debug("createConnection() building connection with URL: {}", url);

        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
        props.setProperty("allowPublicKeyRetrieval", "true");
        props.setProperty("useSSL", "false");
        props.setProperty("serverTimezone", "UTC");
        props.setProperty("socketTimeout", String.valueOf(socketTimeoutMs));

        try {
            Connection conn = DriverManager.getConnection(url, props);
            logger.info("createConnection() established new connection: {}", conn);
            return conn;
        } catch (SQLException e) {
            logger.error("Failed to create database connection", e);
            throw e;
        }
    }

    private String buildUrl() {
        String url = String.format("jdbc:mysql://%s:%d/%s", host, port, database);
        logger.trace("buildUrl() constructed URL: {}", url);
        return url;
    }

    public void shutdown() {
        logger.info("shutdown() called - closing all {} connections",
                pool == null ? 0 : pool.size());
        if (pool != null) {
            pool.forEach(c -> {
                try {
                    if (!c.isClosed()) {
                        c.close();
                        logger.debug("Closed connection during shutdown: {}", c);
                    } else {
                        logger.trace("Connection already closed during shutdown: {}", c);
                    }
                } catch (SQLException e) {
                    logger.error("Error closing connection", e);
                }
            });
            pool.clear();
            logger.info("Connection pool shutdown complete");
        } else {
            logger.warn("shutdown() called but pool was null");
        }
    }
}

