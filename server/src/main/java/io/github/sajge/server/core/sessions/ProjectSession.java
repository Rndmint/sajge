package io.github.sajge.server.core.sessions;

import io.github.sajge.server.core.comm.Request;
import io.github.sajge.server.core.comm.Response;
import io.github.sajge.server.core.services.ServiceSupport;

public class ProjectSession extends ServiceSupport {
    @Override
    public boolean canProcess(Request request) {
        return request.isAuthenticated() && "PROJECT".equalsIgnoreCase(request.getPayload());
    }

    @Override
    public Response process(Request request) {
        return new Response("Project list");
    }
}
