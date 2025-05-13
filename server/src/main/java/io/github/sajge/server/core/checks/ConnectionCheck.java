package io.github.sajge.server.core.checks;

import io.github.sajge.server.core.comm.Request;
import io.github.sajge.server.core.comm.Response;
import io.github.sajge.server.core.services.ServiceSupport;

public class ConnectionCheck extends ServiceSupport {
    @Override
    public boolean canProcess(Request request) {
        return "CONNECT".equalsIgnoreCase(request.getPayload());
    }

    @Override
    public Response process(Request request) {
        return new Response("OK Connected");
    }
}
