package io.github.sajge.server.core.checks;

import io.github.sajge.server.core.comm.Request;
import io.github.sajge.server.core.comm.Response;
import io.github.sajge.server.core.services.ServiceSupport;

public class AuthenticationCheck extends ServiceSupport {
    @Override
    public boolean canProcess(Request request) {
        if (request.isAuthenticated()) {
            return false;
        }
        return request.getPayload().toUpperCase().startsWith("AUTH ");
    }

    @Override
    public Response process(Request request) {
        String token = request.getPayload().substring(5).trim();
        if ("secret".equals(token)) {
            request.setAuthenticated(true);
            return new Response("OK Auth successful");
        }
        return new Response("ERR Auth failed");
    }
}
