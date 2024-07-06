package com.tanvir.tenantmanagement.master;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Slf4j
@Component
public class MasterRepositoryConnection {

    @Value("${multi-tenant-service.r2dbc.host}")
    private String host;

    @Value("${multi-tenant-service.r2dbc.port}")
    private Integer port;

    @Value("${multi-tenant-service.r2dbc.username}")
    private String username;

    @Value("${multi-tenant-service.r2dbc.password}")
    private String password;

    @Value("${multi-tenant-service.r2dbc.database}")
    private String database;

    @Value("${multi-tenant-service.r2dbc.master.schema}")
    private String schema;
    private Connection con;

    public Connection connectDB() {
        try {
            con = DriverManager.getConnection(buildConnectionUrl(), username, password);
            log.info("Successfully established connection to {}", buildConnectionUrl());
            return con;
        } catch (SQLException e) {
            log.error("Failed to establish connection:", e);
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public void closeConnection() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.error("Failed to close connection:", e);
            }
        }
    }


    private String buildConnectionUrl() {
        return "jdbc:postgresql://" +
                host +
                ":" +
                port +
                "/" +
                database +
                "?currentSchema=" +
                schema;
    }
}
