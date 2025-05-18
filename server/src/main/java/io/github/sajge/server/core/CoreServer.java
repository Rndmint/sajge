package io.github.sajge.server.core;

import io.github.sajge.logger.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CoreServer implements Runnable {
    private static final Logger log = Logger.get(CoreServer.class);

    private final ExecutorService executor;
    private final ServerSocket serverSocket;
    private volatile boolean running = true;

    public CoreServer() throws Exception {
        this.serverSocket = new ServerSocket(ServiceContext.getServerConfig().getServerPort());
        this.executor = Executors.newCachedThreadPool();
        log.info("CoreServer started on port {}", ServiceContext.getServerConfig().getServerPort());
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                log.debug("Accepted connection from {}", clientSocket.getRemoteSocketAddress());
                executor.submit(new RequestHandler(clientSocket));
            } catch (Exception e) {
                if (running) {
                    log.error("Error accepting connection", e);
                }
            }
        }
    }

    public void stop() throws Exception {
        running = false;
        serverSocket.close();
        executor.shutdownNow();
        ServiceContext.shutdownPool();
        log.info("CoreServer stopped");
    }
}
