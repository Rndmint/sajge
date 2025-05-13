package io.github.sajge.server.core.services;

import io.github.sajge.server.core.comm.Request;
import io.github.sajge.server.core.comm.Response;

public abstract class ServiceSupport implements Service {
    protected Service nextService;

    @Override
    public void routeTo(Service next) {
        this.nextService = next;
    }

    @Override
    public Response route(Request request) {
        if (canProcess(request)) {
            return process(request);
        }
        if (nextService != null) {
            return nextService.route(request);
        }
        throw new IllegalStateException("No service for " + request.getPayload());
    }
}
