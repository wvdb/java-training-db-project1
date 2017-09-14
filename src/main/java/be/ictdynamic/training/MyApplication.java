package be.ictdynamic.training;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by wvdbrand on 14/09/2017.
 */
public class MyApplication {

    public static void main(String args[]) {
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");

            System.out.println("Opened database successfully");
            createTables(connection);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(-1);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }

    }

    private static void createTables(Connection connection) {
        Statement stmt = null;

        try {
            stmt = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS EMPLOYEE"  +
                                "(ID INT PRIMARY KEY            NOT NULL,"  +
                                " NAME           VARCHAR(50)    NOT NULL, " +
                                " AGE            INT            NOT NULL, " +
                                " ADDRESS        CHAR(50),                " +
                                " SALARY         REAL)";
            stmt.executeUpdate(sql);
            stmt.close();

            System.out.println("Table EMPLOYEE has been created successfully");
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

}
