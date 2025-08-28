/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ADMIN
 */
public class dbconnect {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Flight_Booking_Web;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASS = "123";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Không tìm thấy JDBC Driver", e);
        }
    }
}
