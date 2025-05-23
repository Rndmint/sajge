package io.github.sajge.client;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class ClientConfig {
    private String serverHost;
    private int serverPort;

    public ClientConfig() {
    }

    public ClientConfig(String resourcePath) {
        Yaml yaml = new Yaml();
        try (InputStream in = getClass()
                .getClassLoader()
                .getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalStateException("Configuration file not found: " + resourcePath);
            }
            ClientConfig loaded = yaml.loadAs(in, ClientConfig.class);
            this.serverHost = loaded.serverHost;
            this.serverPort = loaded.serverPort;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration from " + resourcePath, e);
        }
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}
