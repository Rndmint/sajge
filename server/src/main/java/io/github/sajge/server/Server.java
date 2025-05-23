package io.github.sajge.server;

import io.github.sajge.server.accounts.deletes.DeleteDao;
import io.github.sajge.server.accounts.deletes.DeleteDto;
import io.github.sajge.server.accounts.deletes.DeleteHandler;
import io.github.sajge.server.accounts.deletes.DeleteService;
import io.github.sajge.server.accounts.logouts.LogoutDto;
import io.github.sajge.server.accounts.logouts.LogoutHandler;
import io.github.sajge.server.accounts.users.ListUsersDto;
import io.github.sajge.server.accounts.users.ListUsersHandler;
import io.github.sajge.server.accounts.users.UserDao;
import io.github.sajge.server.accounts.users.UserService;
import io.github.sajge.server.config.ServerConfig;
import io.github.sajge.server.echos.EchoDto;
import io.github.sajge.server.accounts.logins.LoginDao;
import io.github.sajge.database.DBConnectionPool;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.server.accounts.logins.LoginDto;
import io.github.sajge.server.accounts.logins.LoginHandler;
import io.github.sajge.server.accounts.logins.LoginService;
import io.github.sajge.server.accounts.signups.SignupDto;
import io.github.sajge.server.accounts.signups.SignupService;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.accounts.signups.SignupDao;
import io.github.sajge.server.echos.EchoHandler;
import io.github.sajge.server.accounts.signups.SignupHandler;
import io.github.sajge.server.network.Dispatcher;
import io.github.sajge.server.projects.ProjectDao;
import io.github.sajge.server.projects.ProjectService;
import io.github.sajge.server.projects.creates.CreateProjectDto;
import io.github.sajge.server.projects.creates.CreateProjectHandler;
import io.github.sajge.server.projects.deletes.DeleteProjectDto;
import io.github.sajge.server.projects.deletes.DeleteProjectHandler;
import io.github.sajge.server.projects.invites.InviteCollaboratorDto;
import io.github.sajge.server.projects.invites.InviteCollaboratorHandler;
import io.github.sajge.server.projects.invites.accepts.AcceptInviteDto;
import io.github.sajge.server.projects.invites.accepts.AcceptInviteHandler;
import io.github.sajge.server.projects.invites.refuses.RefuseInviteDto;
import io.github.sajge.server.projects.invites.refuses.RefuseInviteHandler;
import io.github.sajge.server.projects.lists.ListOwnedProjectsDto;
import io.github.sajge.server.projects.lists.ListOwnedProjectsHandler;
import io.github.sajge.server.projects.lists.collaborated.ListCollaboratedProjectsDto;
import io.github.sajge.server.projects.lists.collaborated.ListCollaboratedProjectsHandler;
import io.github.sajge.server.projects.lists.collaborators.ListProjectCollaboratorsDto;
import io.github.sajge.server.projects.lists.collaborators.ListProjectCollaboratorsHandler;
import io.github.sajge.server.projects.lists.invited.ListSentInvitesDto;
import io.github.sajge.server.projects.lists.invited.ListSentInvitesHandler;
import io.github.sajge.server.projects.lists.pendings.ListPendingInvitesDto;
import io.github.sajge.server.projects.lists.pendings.ListPendingInvitesHandler;
import io.github.sajge.server.projects.lists.projectncollab.ListOwnedProjectsWithCollaboratorsDto;
import io.github.sajge.server.projects.lists.projectncollab.ListOwnedProjectsWithCollaboratorsHandler;
import io.github.sajge.server.projects.removes.RemoveCollaboratorDto;
import io.github.sajge.server.projects.removes.RemoveCollaboratorHandler;
import io.github.sajge.server.projects.updates.UpdateProjectDto;
import io.github.sajge.server.projects.updates.UpdateProjectHandler;
import io.github.sajge.server.sessionchecks.CheckSessionDto;
import io.github.sajge.server.sessionchecks.CheckSessionHandler;

import java.sql.SQLException;
import java.util.Map;

import static java.util.Map.entry;

public class Server {
    private static final Logger logger = Logger.get(Server.class);
    private static final int SERVER_PORT;
    private static final int SERVER_ACCEPT_TIMEOUT_MS;
    private static final int SOCKET_READ_TIMEOUT_MS;
    private static final int WORKER_POOL_SIZE;

    public void start() {
        try {
            logger.info("Starting server on port {}", SERVER_PORT);

            var routes = Map.ofEntries(
                    entry(
                            RequestType.ECHO,
                            new Dispatcher.Route(
                                    new EchoHandler(), EchoDto.class)),
                    entry(
                            RequestType.SIGNUP,
                            new Dispatcher.Route(
                                    new SignupHandler(
                                            new SignupService(
                                                    new SignupDao())), SignupDto.class)),
                    entry(
                            RequestType.LOGIN,
                            new Dispatcher.Route(
                                    new LoginHandler(
                                            new LoginService(
                                                    new LoginDao())), LoginDto.class)),
                    entry(
                            RequestType.LOGOUT,
                            new Dispatcher.Route(
                                    new LogoutHandler(), LogoutDto.class)),
                    entry(
                            RequestType.CHECK_SESSION,
                            new Dispatcher.Route(
                                    new CheckSessionHandler(), CheckSessionDto.class)),
                    entry(
                            RequestType.DELETE_ACCOUNT,
                            new Dispatcher.Route(
                                    new DeleteHandler(
                                            new DeleteService(
                                                    new DeleteDao())), DeleteDto.class)),
                    entry(
                            RequestType.CREATE_PROJECT,
                            new Dispatcher.Route(
                                    new CreateProjectHandler(
                                            new ProjectService(
                                                    new ProjectDao())), CreateProjectDto.class)),
                    entry(
                            RequestType.UPDATE_PROJECT,
                            new Dispatcher.Route(
                                    new UpdateProjectHandler(
                                            new ProjectService(
                                                    new ProjectDao())), UpdateProjectDto.class)),
                    entry(
                            RequestType.DELETE_PROJECT,
                            new Dispatcher.Route(
                                    new DeleteProjectHandler(
                                            new ProjectService(
                                                    new ProjectDao())), DeleteProjectDto.class)),
                    entry(
                            RequestType.INVITE_COLLABORATOR,
                            new Dispatcher.Route(
                                    new InviteCollaboratorHandler(
                                            new ProjectService(
                                                    new ProjectDao())), InviteCollaboratorDto.class)),
                    entry(
                            RequestType.ACCEPT_INVITE,
                            new Dispatcher.Route(
                                    new AcceptInviteHandler(
                                            new ProjectService(
                                                    new ProjectDao())), AcceptInviteDto.class)),
                    entry(
                            RequestType.REMOVE_COLLABORATOR,
                            new Dispatcher.Route(
                                    new RemoveCollaboratorHandler(
                                            new ProjectService(
                                                    new ProjectDao())), RemoveCollaboratorDto.class)),
                    entry(
                            RequestType.REFUSE_INVITE,
                            new Dispatcher.Route(
                                    new RefuseInviteHandler(
                                            new ProjectService(
                                                    new ProjectDao())), RefuseInviteDto.class)),
                    entry(
                            RequestType.LIST_OWNED_PROJECTS,
                            new Dispatcher.Route(
                                    new ListOwnedProjectsHandler(
                                            new ProjectService(
                                                    new ProjectDao())), ListOwnedProjectsDto.class)),
                    entry(
                            RequestType.LIST_PENDING_INVITES,
                            new Dispatcher.Route(
                                    new ListPendingInvitesHandler(
                                            new ProjectService(
                                                    new ProjectDao())), ListPendingInvitesDto.class)),
                    entry(
                            RequestType.LIST_SENT_INVITES,
                            new Dispatcher.Route(
                                    new ListSentInvitesHandler(
                                            new ProjectService(
                                                    new ProjectDao())), ListSentInvitesDto.class)),
                    entry(
                            RequestType.LIST_USERS,
                            new Dispatcher.Route(
                                    new ListUsersHandler(
                                            new UserService(
                                                    new UserDao())), ListUsersDto.class)),
                    entry(
                            RequestType.LIST_COLLABORATED_PROJECTS,
                            new Dispatcher.Route(
                                    new ListCollaboratedProjectsHandler(
                                            new ProjectService(
                                                    new ProjectDao())), ListCollaboratedProjectsDto.class)),
                    entry(
                            RequestType.LIST_PROJECT_COLLABORATORS,
                            new Dispatcher.Route(
                                    new ListProjectCollaboratorsHandler(
                                            new ProjectService(
                                                    new ProjectDao())), ListProjectCollaboratorsDto.class)),
                    entry(
                            RequestType.LIST_OWNED_PROJECTS_WITH_COLLABORATORS,
                            new Dispatcher.Route(
                                    new ListOwnedProjectsWithCollaboratorsHandler(
                                            new ProjectService(
                                                    new ProjectDao())), ListOwnedProjectsWithCollaboratorsDto.class))
            );

            Dispatcher dispatcher = new Dispatcher(
                    SERVER_ACCEPT_TIMEOUT_MS,
                    SOCKET_READ_TIMEOUT_MS,
                    WORKER_POOL_SIZE,
                    routes);

            dispatcher.start(SERVER_PORT);
            logger.info("Server started successfully");
        } catch (Exception e) {
            logger.error("Failed to start server", e);
            System.exit(1);
        }
    }

    static {
        try {
            ServerConfig cfg = new ServerConfig("server_config.yaml");

            DBConnectionPool.INSTANCE.init(
                    cfg.getDBHost(),
                    cfg.getDBPort(),
                    cfg.getDBName(),
                    cfg.getDBQueryTimeoutMillis(),
                    cfg.getDBUser(),
                    cfg.getDBPassword(),
                    cfg.getDBConnectionPoolSize()
            );

            SERVER_PORT = cfg.getServerPort();
            SERVER_ACCEPT_TIMEOUT_MS = cfg.getServerSocketTimeoutMillis();
            SOCKET_READ_TIMEOUT_MS = cfg.getServerSocketTimeoutMillis();
            WORKER_POOL_SIZE = cfg.getThreadPoolSize();

        } catch (Exception e) {
            logger.error("Failed to load server configuration", e);
            throw new ExceptionInInitializerError(e);
        }
    }
}
