/*
 * Click nbfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database context for managing SQL Server connections and queries.
 * @author LienXuanThinh - CE182117
 */
public class DBContext {
    private static final String URL = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=Flight_Booking_Web;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASS = "123456";
    private static final Logger LOGGER = Logger.getLogger(DBContext.class.getName());

    public DBContext() {
        // No connection initialization here
    }

    public void testConnect() {
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn == null || conn.isClosed()) {
                LOGGER.severe("Database connection test failed: Connection is null or closed");
                System.out.println("Fail");
            } else {
                LOGGER.info("Database connection test successful");
                System.out.println("Ok");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error testing database connection", e);
            System.out.println("Fail: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error closing test connection", e);
                }
            }
        }
    }

    // Get a new database connection
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            LOGGER.info("New database connection established");
            return conn;
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "JDBC driver not found", e);
            throw new SQLException("Error loading JDBC driver: " + e.getMessage(), e);
        }
    }

    // Execute SELECT query with parameters
    public ResultSet execSelectQuery(String query, Object[] params) throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            preparedStatement = conn.prepareStatement(query);
            LOGGER.info("Executing SELECT query: " + query);

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                    LOGGER.fine("Parameter " + (i + 1) + ": " + params[i]);
                }
            }

            rs = preparedStatement.executeQuery();
            // Return ResultSet; caller is responsible for closing ResultSet, PreparedStatement, and Connection
            return rs;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error executing SELECT query: " + query, e);
            throw e;
        } finally {
            // Do not close PreparedStatement or Connection here; caller will close them
        }
    }

    // Execute SELECT query without parameters
    public ResultSet execSelectQuery(String query) throws SQLException {
        return execSelectQuery(query, null);
    }

    // Execute INSERT, UPDATE, DELETE query
    public int execQuery(String query, Object[] params) throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = getConnection();
            preparedStatement = conn.prepareStatement(query);
            LOGGER.info("Executing query: " + query);

            if (params != null) {
                LOGGER.info("Parameters: ");
                for (int i = 0; i < params.length; i++) {
                    LOGGER.fine("Parameter " + (i + 1) + ": " + params[i]);
                    if (params[i] instanceof Integer) {
                        preparedStatement.setInt(i + 1, (Integer) params[i]);
                    } else if (params[i] instanceof String) {
                        preparedStatement.setString(i + 1, (String) params[i]);
                    } else if (params[i] instanceof Double) {
                        preparedStatement.setDouble(i + 1, (Double) params[i]);
                    } else if (params[i] instanceof java.sql.Date) {
                        preparedStatement.setDate(i + 1, (java.sql.Date) params[i]);
                    } else if (params[i] instanceof Boolean) {
                        preparedStatement.setBoolean(i + 1, (Boolean) params[i]);
                    } else {
                        preparedStatement.setObject(i + 1, params[i]);
                    }
                }
            }

            int rowsAffected = preparedStatement.executeUpdate();
            LOGGER.info("Rows affected: " + rowsAffected);
            return rowsAffected;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error executing query: " + query, e);
            throw e;
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error closing PreparedStatement", e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error closing Connection", e);
                }
            }
        }
    }

    // Close resources explicitly (for use with execSelectQuery)
    public static void closeSelectResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing ResultSet", e);
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing PreparedStatement", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing Connection", e);
            }
        }
    }

    public static void main(String[] args) {
        DBContext db = new DBContext();
        db.testConnect();
    }
}