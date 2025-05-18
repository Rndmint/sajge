package io.github.sajge.client.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.client.NetworkConfig;
import io.github.sajge.client.responses.Response;
import io.github.sajge.logger.Logger;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public abstract class ClientRequest<I, O> extends Thread {
    private static final Logger logger = Logger.get(ClientRequest.class);

    private final NetworkConfig networkConfig;
    private final Request requestPayload;
    private final I requestInfo;
    private final byte[] serializedRequest;
    private O result;

    public ClientRequest(NetworkConfig networkConfig,
                         int threadSuffix,
                         Request requestPayload,
                         I requestInfo,
                         byte[] serializedRequest) {
        super("ClientRequest-" + threadSuffix);
        this.networkConfig = networkConfig;
        this.requestPayload = requestPayload;
        this.requestInfo = requestInfo;
        this.serializedRequest = serializedRequest;
        start();
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(
                networkConfig.getHost(),
                networkConfig.getPort());
             OutputStream outStream = socket.getOutputStream();
             InputStream inStream = socket.getInputStream()) {

            logger.debug("Connecting to {}:{}",
                    networkConfig.getHost(),
                    networkConfig.getPort());

            outStream.write(serializedRequest);
            socket.shutdownOutput();

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] chunk = new byte[8_192];
            int bytesRead;
            while ((bytesRead = inStream.read(chunk)) != -1) {
                buffer.write(chunk, 0, bytesRead);
            }
            String responseJson = buffer.toString(StandardCharsets.UTF_8);
            logger.trace("Raw response JSON: {}", responseJson);

            ObjectMapper mapper = new ObjectMapper();
            Response response = mapper.readValue(responseJson, Response.class);
            if (!response.isSuccess()) {
                throw new RuntimeException("Server error: " + response.getError());
            }

            this.result = readResult(response.getBody());

        } catch (Exception e) {
            logger.error("Error in client request {}", requestPayload.getRequestId(), e);
            throw new RuntimeException(e);
        }
    }

    protected abstract O readResult(String body) throws IOException;

    public O getResult() {
        return result;
    }

    public I getRequestInfo() {
        return requestInfo;
    }
}
