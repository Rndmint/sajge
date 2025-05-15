package io.github.sajge.server;

import io.github.sajge.dao.LoginDao;
import io.github.sajge.database.DBConnectionPool;
import io.github.sajge.message.Request;
import io.github.sajge.server.handler.LoginHandler;
import io.github.sajge.services.LoginService;
import io.github.sajge.services.SignupService;
import io.github.sajge.logger.Logger;
import io.github.sajge.dao.SignupDao;
import io.github.sajge.server.handler.EchoHandler;
import io.github.sajge.server.handler.SignupHandler;
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

            SignupService signupService = new SignupService(new SignupDao());

            EchoHandler echoHandler = new EchoHandler();
            SignupHandler signupHandler = new SignupHandler(signupService);

            LoginService loginService = new LoginService(new LoginDao());
            LoginHandler loginHandler = new LoginHandler(loginService);

            Dispatcher dispatcher = new Dispatcher(
                    SERVER_ACCEPT_TIMEOUT_MS,
                    SOCKET_READ_TIMEOUT_MS,
                    WORKER_POOL_SIZE,
                    Map.of(
                            Request.ECHO, echoHandler,
                            Request.SIGNUP, signupHandler,
                            Request.LOGIN,  loginHandler
            ));

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
