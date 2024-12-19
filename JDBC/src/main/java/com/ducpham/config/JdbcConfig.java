package com.ducpham.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConfig {
    private static final String hostName = "localhost";
    private static final String databaseName = "pdb_learning";
    private static final String username = "sys as sysdba";
    public static final String password = "30122002";
    private static final String port = "1521";

    private static final String url = "jdbc:oracle:thin:@//" + hostName + ":" + port + "/" + databaseName;

    public static Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        return DriverManager.getConnection(url, username, password);
    }
}
