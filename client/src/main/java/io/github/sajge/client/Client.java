package io.github.sajge.client;

import io.github.sajge.logger.Logger;
import java.net.Socket;

public class Client {
    private static final Logger log = Logger.get(Client.class);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 5; i++) {
            try (Socket sock = new Socket("localhost", 12345)) {
                log.info("Iter {}: connected", i);
            } catch (Exception e) {
                log.error("Iter {}: failed", i, e);
            }
            Thread.sleep(1000);
        }
    }
}
