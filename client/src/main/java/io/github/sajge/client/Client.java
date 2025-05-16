package io.github.sajge.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.sajge.logger.Logger;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 8080;
        ObjectMapper mapper = new ObjectMapper();

        try (Socket socket = new Socket(host, port);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 1) SIGNUP
            sendSimple(mapper, writer, reader, "SIGNUP", obj -> {
                obj.put("username", "testuser");
                obj.put("password", "testpass");
            });

            // 2) LOGIN and extract token
            JsonNode loginResp = sendSimple(mapper, writer, reader, "LOGIN", obj -> {
                obj.put("username", "testuser");
                obj.put("password", "testpass");
            });
            String token = loginResp.path("payload").path("token").asText();
            System.out.println("Extracted token: " + token + "\n");

            // 3) BATCH: ECHO, OPEN_PROJECT, DELETE PROJECT, FETCH_PINGS
            ObjectNode batchPayload = mapper.createObjectNode();
            batchPayload.put("token", token);
            ArrayNode ops = batchPayload.putArray("ops");

            // 3a) ECHO via batch
            ObjectNode opEcho = ops.addObject();
            opEcho.put("op", "ECHO");
            opEcho.put("entity", "USER"); // entity is arbitrary here
            ObjectNode dataEcho = opEcho.putObject("data");
            dataEcho.put("message", "Hello via Batch!");

            // 3b) OPEN_PROJECT
            ObjectNode opOpen = ops.addObject();
            opOpen.put("op", "OPEN_PROJECT");
            opOpen.put("entity", "PROJECT");
            ObjectNode dataOpen = opOpen.putObject("data");
            dataOpen.put("projectId", 1);

            // 3c) DELETE PROJECT
            ObjectNode opDel = ops.addObject();
            opDel.put("op", "DELETE");
            opDel.put("entity", "PROJECT");
            ObjectNode dataDel = opDel.putObject("data");
            dataDel.put("projectId", 2);

            // 3d) FETCH_PINGS
            ObjectNode opPing = ops.addObject();
            opPing.put("op", "FETCH_PINGS");
            opPing.put("entity", "USER");
            opPing.putObject("data");

            ObjectNode batchEnv = mapper.createObjectNode();
            batchEnv.put("type", "BATCH");
            batchEnv.set("payload", batchPayload);

            String batchJson = mapper.writeValueAsString(batchEnv);
            writer.write(batchJson);
            writer.newLine();
            writer.flush();
            System.out.println("Sent BATCH: " + batchJson);

            String batchResp = reader.readLine();
            System.out.println("Received: " + batchResp + "\n");
        }
    }

    private static JsonNode sendSimple(ObjectMapper mapper,
                                       BufferedWriter writer,
                                       BufferedReader reader,
                                       String type,
                                       PayloadBuilder builder) throws IOException {
        ObjectNode env = mapper.createObjectNode();
        env.put("type", type);
        ObjectNode payload = env.putObject("payload");
        builder.build(payload);

        String json = mapper.writeValueAsString(env);
        writer.write(json);
        writer.newLine();
        writer.flush();
        System.out.println("Sent: " + json);

        String resp = reader.readLine();
        System.out.println("Received: " + resp + "\n");
        return mapper.readTree(resp);
    }

    @FunctionalInterface
    interface PayloadBuilder {
        void build(ObjectNode node);
    }
}
