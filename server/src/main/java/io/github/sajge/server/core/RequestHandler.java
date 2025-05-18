package io.github.sajge.server.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.net.Request;
import io.github.sajge.server.net.Response;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger log = Logger.get(RequestHandler.class);

    private final Socket clientSocket;

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (Socket socket = clientSocket;
             BufferedInputStream bufferedIn = new BufferedInputStream(socket.getInputStream());
             BufferedOutputStream bufferedOut = new BufferedOutputStream(socket.getOutputStream())) {

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] chunk = new byte[8192];
            int bytesRead;
            while ((bytesRead = bufferedIn.read(chunk)) != -1) {
                buffer.write(chunk, 0, bytesRead);
            }
            byte[] requestBytes = buffer.toByteArray();
            log.debug("Received {} bytes from {}", requestBytes.length, socket.getRemoteSocketAddress());

            ObjectMapper mapper = new ObjectMapper();
            Request request = mapper.readValue(requestBytes, Request.class);
            log.debug("Parsed request: {}", request.getAction());

            Response response = ServiceRouter.handle(request);
            response.setRequestId(request.getRequestId());

            byte[] responseBytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(response);

            int offset = 0;
            while (offset < responseBytes.length) {
                int length = Math.min(chunk.length, responseBytes.length - offset);
                bufferedOut.write(responseBytes, offset, length);
                offset += length;
            }
            bufferedOut.flush();
            log.debug("Sent response for requestId {}", response.getRequestId());

        } catch (Exception e) {
            log.error("Error handling client request", e);
        } finally {
            ServiceContext.releaseConnection();
        }
    }
}
