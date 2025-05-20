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

            String jsonPayload = null;
            //jsonPayload = "{\"type\":\"SIGNUP\",\"payload\":{\"username\":\"someone\",\"password\":\"testPass\"}}";
            //jsonPayload = "{\"type\":\"LOGIN\",\"payload\":{\"username\":\"someone\",\"password\":\"testPass\"}}";
            //jsonPayload = "{\"type\":\"CHECK_SESSION\",\"payload\":{\"token\":\"18ca3eb5-da4b-41c2-95d0-c44f429cbed9\"}}";
            //jsonPayload = "{\"type\":\"DELETE_ACCOUNT\", \"payload\":{\"username\":\"alice\",\"token\":\"393012b1-480d-4e0d-8df4-067276006085\"}}";

            //jsonPayload = "{\"type\":\"CREATE_PROJECT\",\"payload\":{\"token\":\"470dc68a-206d-4c1f-bdbb-f18f383a6768\",\"name\":\"Just A Project\",\"description\":\"A test project\",\"sceneJson\":\"{}\"}}";
            //jsonPayload = "{\"type\":\"UPDATE_PROJECT\",\"payload\":{\"token\":\"332a12d2-8b0e-4fb8-a877-461602f9becf\",\"projectId\":2,\"name\":\"Just A Project (v3)\",\"description\":\"Updated description\",\"sceneJson\":\"{}\"}}";
            //jsonPayload = "{\"type\":\"DELETE_PROJECT\",\"payload\":{\"token\":\"332a12d2-8b0e-4fb8-a877-461602f9becf\",\"projectId\":2}}";
            //jsonPayload = "{\"type\":\"INVITE_COLLABORATOR\",\"payload\":{\"token\":\"b7c6264c-7acf-4735-b99e-8877280fcf55\",\"projectId\":2,\"collaboratorUserId\":2,\"canEdit\":true}}";
            //jsonPayload = "{\"type\":\"ACCEPT_INVITE\",\"payload\":{\"token\":\"18ca3eb5-da4b-41c2-95d0-c44f429cbed9\",\"accessKey\":\"545a1697-f61c-4bb6-a8a9-747f3a97c7ae\"}}";

            //jsonPayload = "{\"type\":\"REMOVE_COLLABORATOR\",\"payload\":{\"token\":\"38ee348a-c8f1-460c-a758-745b02d9c073\",\"projectId\":2,\"collaboratorUserId\":2}}";
            //jsonPayload = "{\"type\":\"REFUSE_INVITE\",\"payload\":{\"token\":\"18ca3eb5-da4b-41c2-95d0-c44f429cbed9\",\"accessKey\":\"545a1697-f61c-4bb6-a8a9-747f3a97c7ae\"}}";

            //jsonPayload = "{\"type\":\"LIST_OWNED_PROJECTS\",\"payload\":{\"token\":\"b7c6264c-7acf-4735-b99e-8877280fcf55\"}}";

            jsonPayload = "{\"type\":\"LIST_PENDING_INVITES\",\"payload\":{\"token\":\"147cfca5-df12-43a7-8a52-f281f9be37ee\"}}";

            out.println(jsonPayload);

            String response = in.readLine();
            logger.info("Server response: {}", response);
        } catch (Exception e) {
            logger.error("Client error", e);
        }
    }
}
