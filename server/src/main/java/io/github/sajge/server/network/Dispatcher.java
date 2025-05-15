package io.github.sajge.server.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.message.Request;
import io.github.sajge.message.Message;
import io.github.sajge.server.handler.Handler;
import io.github.sajge.logger.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.*;

public class Dispatcher {
    private static final Logger logger = Logger.get(Dispatcher.class);

    private final BlockingQueue<RequestTask> queue = new LinkedBlockingQueue<>();
    private final Map<Request, Handler> handlers;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ExecutorService clientHandlerPool = Executors.newCachedThreadPool();
    private final ExecutorService workerPool;

    private final int serverAcceptTimeoutMs;
    private final int socketReadTimeoutMs;
    private final int workerPoolSize;

    public Dispatcher(int serverAcceptTimeoutMs,
                      int socketReadTimeoutMs,
                      int workerPoolSize,
                      Map<Request, Handler> handlers) {
        this.handlers = handlers;
        this.serverAcceptTimeoutMs = serverAcceptTimeoutMs;
        this.socketReadTimeoutMs = socketReadTimeoutMs;
        this.workerPoolSize = workerPoolSize;
        this.workerPool = Executors.newFixedThreadPool(workerPoolSize);
    }

    public void start(int port) throws Exception {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(port)) {
                server.setSoTimeout(serverAcceptTimeoutMs);
                logger.info("Listening on {}", port);

                while (true) {
                    try {
                        Socket socket = server.accept();
                        socket.setSoTimeout(socketReadTimeoutMs);
                        clientHandlerPool.submit(() -> readLoop(socket));
                    } catch (SocketTimeoutException e) {
                        logger.debug("Accept timed out, continuing...");
                    }
                }
            } catch (Exception e) {
                logger.error("Error in server socket", e);
            }
        }).start();

        for (int i = 0; i < workerPoolSize; i++) {
            workerPool.submit(this::processLoop);
        }
    }

    private void readLoop(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                Message msg = objectMapper.readValue(line, Message.class);
                queue.put(new RequestTask(msg, out));
            }
        } catch (Exception e) {
            logger.error("Error in read loop", e);
        }
    }

    private void processLoop() {
        try {
            while (true) {
                RequestTask task = queue.take();
                Handler handler = handlers.get(task.message().getType());
                if (handler != null) {
                    String response = handler.handle(task.message());
                    if (response != null) {
                        try {
                            BufferedWriter writer = task.writer();
                            writer.write(response);
                            writer.newLine();
                            writer.flush();
                        } catch (IOException e) {
                            logger.error("Error writing response", e);
                        }
                    }
                } else {
                    logger.warn("No handler for {}", task.message().getType());
                }
            }
        } catch (InterruptedException e) {
            logger.warn("Process loop interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    private record RequestTask(Message message, BufferedWriter writer) {
    }
}
