package io.github.sajge.client.requests;

import io.github.sajge.client.NetworkConfig;
import io.github.sajge.client.dto.Project;

public class UpdateProjectClientRequest
        extends ClientRequest<Project, Void> {
    public UpdateProjectClientRequest(NetworkConfig config,
                                      int threadSuffix,
                                      Request requestPayload,
                                      Project requestInfo,
                                      byte[] serializedRequest) {
        super(config, threadSuffix, requestPayload, requestInfo, serializedRequest);
    }

    @Override
    protected Void readResult(String body) {
        return null;
    }
}
