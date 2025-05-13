package io.github.sajge.server.core.routing;

import io.github.sajge.server.core.comm.Request;
import io.github.sajge.server.core.comm.Response;
import io.github.sajge.server.core.services.Service;

public class ServerDelegator {
    private final Service entry;

    public ServerDelegator() {
        this.entry = new RouteMapper().getEntry();
    }

    public Response delegate(Request request) {
        try {
            return entry.route(request);
        } catch (Exception e) {
            return new Response("ERR No service");
        }
    }
}
