package io.github.sajge.server;

import io.github.sajge.server.echos.EchoDto;
import io.github.sajge.server.echos.EchoResponseDto;
import io.github.sajge.server.logins.LoginDao;
import io.github.sajge.database.DBConnectionPool;
import io.github.sajge.messages.resquests.RequestType;
import io.github.sajge.server.logins.LoginDto;
import io.github.sajge.server.logins.LoginHandler;
import io.github.sajge.server.logins.LoginService;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.server.signups.SignupDto;
import io.github.sajge.server.signups.SignupService;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.signups.SignupDao;
import io.github.sajge.server.echos.EchoHandler;
import io.github.sajge.server.signups.SignupHandler;
import io.github.sajge.server.network.Dispatcher;

import java.sql.SQLException;
import java.util.Map;

public class Server {
    private static final Logger logger = Logger.get(Server.class);
    private static final int SERVER_PORT = 8080;
    private static final int SERVER_ACCEPT_TIMEOUT_MS = 5000;
    private static final int SOCKET_READ_TIMEOUT_MS = 5000;
    private static final int WORKER_POOL_SIZE = 4;

    public void start() {
        try {
            logger.info("Starting server on port {}", SERVER_PORT);

            var routes = Map.<RequestType, Dispatcher.Route>of(
                    RequestType.ECHO,
                    new Dispatcher.Route(new EchoHandler(),
                                        EchoDto.class),

                    RequestType.SIGNUP,
                    new Dispatcher.Route(new SignupHandler(
                                        new SignupService(
                                                new SignupDao())),
                                                SignupDto.class),

                    RequestType.LOGIN,
                    new Dispatcher.Route(new LoginHandler(
                                        new LoginService(
                                                new LoginDao())),
                                                LoginDto.class)
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
            DBConnectionPool.INSTANCE.init(
                    "172.31.253.73",
                    3306,
                    "sajge_db",
                    5000,
                    "root",
                    "wsxedc",
                    5
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
