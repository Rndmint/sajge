package io.github.sajge.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.dto.User;
import io.github.sajge.server.net.Response;
import io.github.sajge.server.util.Hash;
import io.github.sajge.server.util.Token;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountService {
    private static final Logger log = Logger.get(AccountService.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Response createAccount(Connection connection, String payloadJson) {
        try {
            User incoming = mapper.readValue(payloadJson, User.class);
            PreparedStatement checkStmt = connection.prepareStatement(
                    "SELECT COUNT(*) FROM users WHERE username = ?");
            checkStmt.setString(1, incoming.getUsername());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                return error("Username already exists");
            }

            String hashed = Hash.of(incoming.getPasswordHash());
            String token = Token.generate();
            PreparedStatement insertStmt = connection.prepareStatement(
                    "INSERT INTO users(username, display_name, password_hash, token) VALUES(?,?,?,?)");
            insertStmt.setString(1, incoming.getUsername());
            insertStmt.setString(2, incoming.getDisplayName());
            insertStmt.setString(3, hashed);
            insertStmt.setString(4, token);
            insertStmt.executeUpdate();

            incoming.setPasswordHash(null);
            incoming.setToken(token);
            log.info("Created account for user {}", incoming.getUsername());
            return success(incoming);
        } catch (Exception e) {
            log.error("Error creating account", e);
            return error(e.getMessage());
        }
    }

    public static Response login(Connection connection, String payloadJson) {
        try {
            User incoming = mapper.readValue(payloadJson, User.class);
            String hashed = Hash.of(incoming.getPasswordHash());
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT display_name, token FROM users WHERE username = ? AND password_hash = ?");
            stmt.setString(1, incoming.getUsername());
            stmt.setString(2, hashed);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return error("Invalid credentials");
            }
            User user = new User();
            user.setUsername(incoming.getUsername());
            user.setDisplayName(rs.getString(1));
            user.setToken(rs.getString(2));
            log.info("User {} logged in", user.getUsername());
            return success(user);
        } catch (Exception e) {
            log.error("Error during login", e);
            return error(e.getMessage());
        }
    }

    public static User authenticate(Connection connection, String token) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT username, display_name FROM users WHERE token = ?");
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            User user = new User();
            user.setUsername(rs.getString(1));
            user.setDisplayName(rs.getString(2));
            user.setToken(token);
            return user;
        } catch (Exception e) {
            log.error("Error authenticating token", e);
            return null;
        }
    }

    private static Response success(Object data) {
        try {
            Response response = new Response();
            response.setSuccess(true);
            response.setBody(mapper.writeValueAsString(data));
            return response;
        } catch (Exception e) {
            log.error("Error serializing success response", e);
            return error(e.getMessage());
        }
    }

    private static Response error(String message) {
        Response response = new Response();
        response.setSuccess(false);
        response.setError(message);
        return response;
    }
}
