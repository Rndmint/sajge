package io.github.sajge.common;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        URL res = Main.class
                .getClassLoader()
                .getResource("config.yaml");
        if (res == null) {
            throw new IllegalStateException("config.yaml not found");
        }

        Path path = Paths.get(res.toURI());
        Config cfg = ConfigLoader.load(path.toString(), Config.class);
        System.out.println(cfg);
    }

    public static class Config {
        public Server server;
        public Database database;

        @Override
        public String toString() {
            return "Config{" +
                    "server=" + server +
                    ", database=" + database +
                    '}';
        }
    }

    public static class Server {
        public String host;
        public int port;

        @Override
        public String toString() {
            return "Server{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    '}';
        }
    }

    public static class Database {
        public String url;
        public String user;
        public String password;

        @Override
        public String toString() {
            return "Database{" +
                    "url='" + url + '\'' +
                    ", user='" + user + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

}
