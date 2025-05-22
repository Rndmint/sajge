package io.github.sajge.client.demos;

import io.github.sajge.logger.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.Socket;

public class EchoClient {
    private static final Logger logger = Logger.get(EchoClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        String json = "{\"type\":\"ECHO\",\"payload\":\"Hello\"}";

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.write(json);
            out.newLine();
            out.flush();
            logger.info("Sent: {}", json);

            String response = in.readLine();
            logger.info("Received: {}", response);

        } catch (IOException e) {
            logger.error("I/O error in EchoClient", e);
        }
    }
}
