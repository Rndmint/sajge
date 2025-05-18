package io.github.sajge.client.requests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.client.NetworkConfig;
import io.github.sajge.client.dto.Project;

import java.io.IOException;
import java.util.List;

public class GetProjectsClientRequest
        extends ClientRequest<String, List<Project>> {
    public GetProjectsClientRequest(NetworkConfig config,
                                    int threadSuffix,
                                    Request requestPayload,
                                    String token,
                                    byte[] serializedRequest) {
        super(config, threadSuffix, requestPayload, token, serializedRequest);
    }

    @Override
    protected List<Project> readResult(String body) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(body, new TypeReference<List<Project>>() {
        });
    }
}
