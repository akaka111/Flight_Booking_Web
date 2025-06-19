/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
 *
 * @author $ LienXuanThinh - CE182117
 */
public class DBContext {
 private static String url="jdbc:sqlserver://localhost:1433;"
            + "databaseName=Flight_Booking_Web;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";
    private static final String user="sa";
    private static final String pass="0123456";
    public DBContext() {
        // Không tạo kết nối ở đây nữa
    }
    public void testConnect(){
        try {
            Connection conn = getConnection();
            if(conn==null)
                    System.out.println("Fail");
            else
                System.out.println("Ok");
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
    }
    // Phương thức để lấy một kết nối mới
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url,user,pass);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Error loading JDBC driver", ex); // Re-throw as SQLException
        }
    }

    // Phương thức cho các lệnh SELECT (có params)
    public ResultSet execSelectQuery(String query, Object[] params) throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            preparedStatement = conn.prepareStatement(query);

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            rs = preparedStatement.executeQuery();
            return rs;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            throw e; // Re-throw the exception
        } finally {
            // Đóng các tài nguyên theo thứ tự ngược lại
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            // Không đóng connection ở đây nữa, vì ResultSet có thể vẫn cần
        }
    }

    // Phương thức cho các lệnh SELECT không có params
    public ResultSet execSelectQuery(String query) throws SQLException {
        return this.execSelectQuery(query, null);
    }

    // Phương thức cho các lệnh INSERT, UPDATE, DELETE
    public int execQuery(String query, Object[] params) throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = getConnection();
            preparedStatement = conn.prepareStatement(query);

            // In ra câu lệnh SQL và các tham số
            System.out.println("execQuery() - SQL: " + query);
            if (params != null) {
                System.out.print("execQuery() - Parameters: ");
                for (int i = 0; i < params.length; i++) {
                    System.out.print(params[i] + ", ");
                    // Set tham số dựa trên kiểu dữ liệu
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
                System.out.println();
            }

            return preparedStatement.executeUpdate();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    public static void main(String[] args)
    {
        DBContext db = new DBContext();
        db.testConnect();
    }
}
