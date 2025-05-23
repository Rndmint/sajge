package io.github.sajge.server.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class ServerConfig {
    private String DBHost;
    private int DBPort;
    private String DBUser;
    private String DBPassword;
    private String DBName;
    private int DBConnectionPoolSize;
    private int DBQueryTimeoutMillis;
    private int ServerPort;
    private int ServerSocketTimeoutMillis;
    private int ThreadPoolSize;

    public ServerConfig() {
    }

    public ServerConfig(String yamlResourcePath) {
        Yaml yaml = new Yaml();
        try (InputStream in = getClass()
                .getClassLoader()
                .getResourceAsStream(yamlResourcePath)) {
            if (in == null) {
                throw new IllegalStateException("Configuration file not found: " + yamlResourcePath);
            }
            ServerConfig loaded = yaml.loadAs(in, ServerConfig.class);
            copyFrom(loaded);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration from " + yamlResourcePath, e);
        }
    }

    public String getDBHost() {
        return DBHost;
    }

    public void setDBHost(String DBHost) {
        this.DBHost = DBHost;
    }

    public int getDBPort() {
        return DBPort;
    }

    public void setDBPort(int DBPort) {
        this.DBPort = DBPort;
    }

    public String getDBUser() {
        return DBUser;
    }

    public void setDBUser(String DBUser) {
        this.DBUser = DBUser;
    }

    public String getDBPassword() {
        return DBPassword;
    }

    public void setDBPassword(String DBPassword) {
        this.DBPassword = DBPassword;
    }

    public String getDBName() {
        return DBName;
    }

    public void setDBName(String DBName) {
        this.DBName = DBName;
    }

    public int getDBConnectionPoolSize() {
        return DBConnectionPoolSize;
    }

    public void setDBConnectionPoolSize(int size) {
        this.DBConnectionPoolSize = size;
    }

    public int getDBQueryTimeoutMillis() {
        return DBQueryTimeoutMillis;
    }

    public void setDBQueryTimeoutMillis(int millis) {
        this.DBQueryTimeoutMillis = millis;
    }

    public int getServerPort() {
        return ServerPort;
    }

    public void setServerPort(int ServerPort) {
        this.ServerPort = ServerPort;
    }

    public int getServerSocketTimeoutMillis() {
        return ServerSocketTimeoutMillis;
    }

    public void setServerSocketTimeoutMillis(int millis) {
        this.ServerSocketTimeoutMillis = millis;
    }

    public int getThreadPoolSize() {
        return ThreadPoolSize;
    }

    public void setThreadPoolSize(int ThreadPoolSize) {
        this.ThreadPoolSize = ThreadPoolSize;
    }

    private void copyFrom(ServerConfig o) {
        this.DBHost = o.DBHost;
        this.DBPort = o.DBPort;
        this.DBUser = o.DBUser;
        this.DBPassword = o.DBPassword;
        this.DBName = o.DBName;
        this.DBConnectionPoolSize = o.DBConnectionPoolSize;
        this.DBQueryTimeoutMillis = o.DBQueryTimeoutMillis;
        this.ServerPort = o.ServerPort;
        this.ServerSocketTimeoutMillis = o.ServerSocketTimeoutMillis;
        this.ThreadPoolSize = o.ThreadPoolSize;
    }
}
