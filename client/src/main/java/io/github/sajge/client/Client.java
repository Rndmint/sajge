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
            //jsonPayload = "{\"type\":\"LOGIN\",\"payload\":{\"username\":\"dude\",\"password\":\"testPass\"}}";
            //jsonPayload = "{\"type\":\"CHECK_SESSION\",\"payload\":{\"token\":\"18ca3eb5-da4b-41c2-95d0-c44f429cbed9\"}}";
            //jsonPayload = "{\"type\":\"DELETE_ACCOUNT\", \"payload\":{\"username\":\"alice\",\"token\":\"393012b1-480d-4e0d-8df4-067276006085\"}}";

            //jsonPayload = "{\"type\":\"CREATE_PROJECT\",\"payload\":{\"token\":\"470dc68a-206d-4c1f-bdbb-f18f383a6768\",\"name\":\"Just A Project\",\"description\":\"A test project\",\"sceneJson\":\"{}\"}}";
            //jsonPayload = "{\"type\":\"UPDATE_PROJECT\",\"payload\":{\"token\":\"332a12d2-8b0e-4fb8-a877-461602f9becf\",\"projectId\":2,\"name\":\"Just A Project (v3)\",\"description\":\"Updated description\",\"sceneJson\":\"{}\"}}";
            //jsonPayload = "{\"type\":\"DELETE_PROJECT\",\"payload\":{\"token\":\"332a12d2-8b0e-4fb8-a877-461602f9becf\",\"projectId\":2}}";
            //jsonPayload = "{\"type\":\"INVITE_COLLABORATOR\",\"payload\":{\"token\":\"b7c6264c-7acf-4735-b99e-8877280fcf55\",\"projectId\":2,\"collaboratorUserId\":2,\"canEdit\":true}}";
            //jsonPayload = "{\"type\":\"ACCEPT_INVITE\",\"payload\":{\"token\":\"d18ef122-ffaa-4210-a064-19cfa1ba9553\",\"accessKey\":\"164fc522-8ce1-4678-b357-e5c43236a73b\"}}";

            //jsonPayload = "{\"type\":\"REMOVE_COLLABORATOR\",\"payload\":{\"token\":\"38ee348a-c8f1-460c-a758-745b02d9c073\",\"projectId\":2,\"collaboratorUserId\":2}}";
            //jsonPayload = "{\"type\":\"REFUSE_INVITE\",\"payload\":{\"token\":\"18ca3eb5-da4b-41c2-95d0-c44f429cbed9\",\"accessKey\":\"545a1697-f61c-4bb6-a8a9-747f3a97c7ae\"}}";

            //jsonPayload = "{\"type\":\"LIST_OWNED_PROJECTS\",\"payload\":{\"token\":\"f9ef1792-088c-416e-bdae-c136cbe97e94\"}}";

            //jsonPayload = "{\"type\":\"LIST_PENDING_INVITES\",\"payload\":{\"token\":\"0c6f78d8-63a8-48fd-84cc-b4068cfc4ab0\"}}";

            //jsonPayload = "{\"type\":\"LIST_SENT_INVITES\",\"payload\":{\"token\":\"1306c3a5-39c7-459f-8007-3cd27977e363\"}}";

            //jsonPayload = "{\"type\":\"LIST_USERS\",\"payload\":{\"token\":\"3847ca6c-20d0-47ce-98a3-a0bc5d79395c\"}}";

            //jsonPayload = "{\"type\":\"LIST_COLLABORATED_PROJECTS\",\"payload\":{\"token\":\"d18ef122-ffaa-4210-a064-19cfa1ba9553\"}}";

            //jsonPayload = "{\"type\":\"LIST_PROJECT_COLLABORATORS\",\"payload\":{\"token\":\"a803ab81-7a78-418a-a76a-694e0ca097db\"}}";

            //jsonPayload = "{\"type\":\"LIST_OWNED_PROJECTS_WITH_COLLABORATORS\",\"payload\":{\"token\":\"a803ab81-7a78-418a-a76a-694e0ca097db\"}}";


            out.println(jsonPayload);

            String response = in.readLine();
            logger.info("Server response: {}", response);
        } catch (Exception e) {
            logger.error("Client error", e);
        }
    }
}
