package io.github.sajge.server.core.routing;

import io.github.sajge.server.core.services.Service;
import io.github.sajge.server.core.checks.ConnectionCheck;
import io.github.sajge.server.core.checks.AuthenticationCheck;
import io.github.sajge.server.core.sessions.ProfileSession;
import io.github.sajge.server.core.sessions.ProjectSession;
import io.github.sajge.server.core.sessions.RendererSession;

public class RouteMapper {
    private final Service entry;

    public RouteMapper() {
        Service connection = new ConnectionCheck();
        Service auth = new AuthenticationCheck();
        Service profile = new ProfileSession();
        Service project = new ProjectSession();
        Service render = new RendererSession();

        connection.routeTo(auth);
        auth.routeTo(profile);
        profile.routeTo(project);
        project.routeTo(render);

        entry = connection;
    }

    public Service getEntry() {
        return entry;
    }
}
