package io.github.sajge.client;

import io.github.sajge.logger.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final Logger logger = Logger.get(Client.class);
    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("localhost", 8080);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            logger.info("Connected to server");

            String jsonPayload = "{\"type\":\"LOGIN\",\"payload\":{\"username\":\"testUser\",\"password\":\"testPass\"}}";
            out.println(jsonPayload);

            String response = in.readLine();
            logger.info("Server response: {}", response);
        } catch (Exception e) {
            logger.error("Client error", e);
        }
    }
}
