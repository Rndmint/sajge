package io.github.sajge.server.core;

import io.github.sajge.logger.Logger;
import io.github.sajge.server.config.ConfigLoader;
import io.github.sajge.server.config.DbConfig;
import io.github.sajge.server.config.ServerConfig;
import io.github.sajge.server.pool.ConnectionPool;

import java.sql.Connection;

public class ServiceContext {
    private static final Logger log = Logger.get(ServiceContext.class);

    private static final DbConfig dbConfig;
    private static final ServerConfig serverConfig;
    private static final ConnectionPool connectionPool;
    private static final ThreadLocal<Connection> threadConnection = new ThreadLocal<>();

    static {
        dbConfig        = ConfigLoader.load(DbConfig.class, "db-config.yaml");
        serverConfig    = ConfigLoader.load(ServerConfig.class, "server-config.yaml");
        try {
            connectionPool = ConnectionPool.getInstance(dbConfig);
            log.info("Initialized connection pool (size={})", dbConfig.getMaxPoolSize());
        } catch (Exception e) {
            log.error("Failed to initialize connection pool", e);
            throw new RuntimeException(e);
        }
    }

    public static ServerConfig getServerConfig() {
        return serverConfig;
    }

    public static Connection acquireConnection() {
        Connection conn = connectionPool.acquire();
        threadConnection.set(conn);
        log.debug("Acquired connection {} for thread {}", conn, Thread.currentThread().getName());
        return conn;
    }

    public static void releaseConnection() {
        Connection conn = threadConnection.get();
        if (conn != null) {
            connectionPool.release(conn);
            log.debug("Released connection {} for thread {}", conn, Thread.currentThread().getName());
            threadConnection.remove();
        }
    }

    public static void shutdownPool() {
        try {
            connectionPool.terminate();
            log.info("Connection pool terminated");
        } catch (Exception e) {
            log.error("Error terminating connection pool", e);
        }
    }
}
