package io.github.sajge.server.network;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.Envelope;
import io.github.sajge.server.patterns.Handler;
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

    private final Map<RequestType, Route> routes;
    private final BlockingQueue<RequestTask> queue = new LinkedBlockingQueue<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExecutorService clientHandlerPool = Executors.newCachedThreadPool();
    private final ExecutorService workerPool;
    private final int serverAcceptTimeoutMs;
    private final int socketReadTimeoutMs;
    private final int workerPoolSize;

    public Dispatcher(int serverAcceptTimeoutMs,
                      int socketReadTimeoutMs,
                      int workerPoolSize,
                      Map<RequestType, Route> routes) {
        this.serverAcceptTimeoutMs = serverAcceptTimeoutMs;
        this.socketReadTimeoutMs = socketReadTimeoutMs;
        this.workerPoolSize = workerPoolSize;
        this.routes = routes;
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
            JavaType rawType = objectMapper.getTypeFactory()
                    .constructParametricType(Envelope.class, RequestType.class, JsonNode.class);
            while ((line = in.readLine()) != null) {
                Envelope<RequestType, JsonNode> raw = objectMapper.readValue(line, rawType);
                Route route = routes.get(raw.getType());
                if (route != null) {
                    Object payload = objectMapper.convertValue(raw.getPayload(), route.getPayloadClass());
                    Envelope<RequestType, Object> env = new Envelope<>();
                    env.setType(raw.getType());
                    env.setPayload(payload);
                    queue.put(new RequestTask(env, out));
                } else {
                    logger.warn("No route for {}", raw.getType());
                }
            }
        } catch (Exception e) {
            logger.error("Error in read loop", e);
        }
    }

    private void processLoop() {
        try {
            while (true) {
                RequestTask task = queue.take();
                Route route = routes.get(task.envelope().getType());
                Handler handler = route.getHandler();
                String response = handler.handle(task.envelope());
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
            }
        } catch (InterruptedException e) {
            logger.warn("Process loop interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    public static class Route {
        private final Handler handler;
        private final Class<?> payloadClass;

        public Route(Handler handler, Class<?> payloadClass) {
            this.handler = handler;
            this.payloadClass = payloadClass;
        }

        public Handler getHandler() {
            return handler;
        }

        public Class<?> getPayloadClass() {
            return payloadClass;
        }
    }

    private static record RequestTask(Envelope<RequestType, Object> envelope, BufferedWriter writer) {}
}
