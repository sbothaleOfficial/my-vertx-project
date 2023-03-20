package com.sbothale.webserver.database;

import org.h2.tools.Server;

import java.sql.SQLException;

public class H2DatabaseServer {
    private static final String DB_NAME = "test";
    private static final String DB_USER = "sbothale";
    private static final String DB_PASSWORD = "abc123";

    private Server server;

    public void start() throws SQLException {
        server = Server.createTcpServer("-tcpPort", "8082", "-tcpAllowOthers", "-ifNotExists",
                "-key", DB_NAME, "-user", DB_USER, "-password", DB_PASSWORD);
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop();
        }
    }

    public String getJdbcUrl() {
        return "jdbc:h2:tcp://localhost:8082/" + DB_NAME;
    }

    public String getDbUser() {
        return DB_USER;
    }

    public String getDbPassword() {
        return DB_PASSWORD;
    }
}
