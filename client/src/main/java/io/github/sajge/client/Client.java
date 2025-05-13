package io.github.sajge.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("localhost", 8080);
             BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String line;
            while ((line = terminal.readLine()) != null) {
                out.println(line.trim());
                String reply = in.readLine();
                System.out.println(reply);
                if ("QUIT".equalsIgnoreCase(line.trim())) {
                    break;
                }
            }
        }
    }
}
