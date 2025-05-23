package io.github.sajge.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.logger.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BaseClient<S, E> {
    private static final Logger logger = Logger.get(BaseClient.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String HOST;
    private static final int PORT;

    protected S lastSuccess;
    protected E lastError;
    private boolean lastOk = false;

    protected <R> void sendRequest(R req,
                                   String successType,
                                   Class<S> successClass,
                                   Class<E> errorClass) throws Exception {
        logger.debug("sendRequest() called with req={}, successType={}, successClass={}, errorClass={}",
                req, successType, successClass, errorClass);

        String jsonReq = MAPPER.writeValueAsString(req)
                .replace("\r", "")
                .replace("\n", "");

        logger.trace("Serialized request JSON: {}", jsonReq);

        logger.debug("Opening socket to {}:{}", HOST, PORT);

        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            logger.trace("Socket opened: {}", socket);

            out.println(jsonReq);

            logger.debug("Sent request: {}", jsonReq);

            logger.debug("Awaiting response from server");

            String respLine = in.readLine();

            if (respLine == null) {
                logger.error("No response from server (connection closed)");
                throw new RuntimeException("No response from server (connection closed)");
            }
            logger.debug("Received response: {}", respLine);

            JsonNode root = MAPPER.readTree(respLine);
            String type = root.get("type").asText();
            JsonNode payload = root.get("payload");
            logger.trace("Response type={}, payload={}", type, payload);

            if (successType.equals(type)) {
                lastSuccess = MAPPER.treeToValue(payload, successClass);
                lastError = null;
                lastOk = true;

                logger.info("Response type {} matches expected success type; parsed success payload={}", type, lastSuccess);
            } else {
                lastError = MAPPER.treeToValue(payload, errorClass);
                lastSuccess = null;
                lastOk = false;

                logger.warn("Response type {} indicates error; parsed error payload={}", type, lastError);
            }
        }
    }

    public boolean isSuccess() {
        return lastOk;
    }

    public S getLastSuccess() {
        return lastSuccess;
    }

    public E getLastError() {
        return lastError;
    }


    static {
        ClientConfig cfg = new ClientConfig("client_config.yaml");
        HOST = cfg.getServerHost();
        PORT = cfg.getServerPort();
        logger.info("BaseClient configured for {}:{}", HOST, PORT);
    }

}
