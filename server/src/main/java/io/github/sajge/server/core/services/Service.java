package io.github.sajge.server.core.services;

import io.github.sajge.server.core.comm.Request;
import io.github.sajge.server.core.comm.Response;

public interface Service {
    boolean canProcess(Request request);
    Response process(Request request);
    void routeTo(Service next);
    Response route(Request request);
}
