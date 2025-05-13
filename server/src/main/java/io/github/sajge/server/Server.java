package io.github.sajge.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import io.github.sajge.server.core.routing.ServerDelegator;
import io.github.sajge.server.core.comm.Request;
import io.github.sajge.server.core.comm.Response;

public class Server {
    private final int port;
    private final ServerDelegator delegator;

    public Server(int port) {
        this.port = port;
        this.delegator = new ServerDelegator();
    }

    public void start() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handle(socket)).start();
            }
        }
    }

    private void handle(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            String line;
            while ((line = in.readLine()) != null) {
                Request request = new Request(socket, line.trim());
                Response response = delegator.delegate(request);
                out.println(response.getPayload());
                if ("QUIT".equalsIgnoreCase(line.trim())) {
                    break;
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new Server(8080).start();
    }
}
