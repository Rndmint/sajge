package io.github.sajge.client.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.client.NetworkConfig;
import io.github.sajge.client.dto.User;

import java.io.IOException;

public class LoginClientRequest
        extends ClientRequest<User, User> {
    public LoginClientRequest(NetworkConfig config,
                              int threadSuffix,
                              Request requestPayload,
                              User requestInfo,
                              byte[] serializedRequest) {
        super(config, threadSuffix, requestPayload, requestInfo, serializedRequest);
    }

    @Override
    protected User readResult(String body) throws IOException {
        return new ObjectMapper().readValue(body, User.class);
    }
}
