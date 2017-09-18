package be.ictdynamic.training;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by wvdbrand on 14/09/2017.
 */
public class MyApplication {

    private static final Logger LOGGER = Logger.getLogger(MyApplication.class);

    public static void main(String args[]) {
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");

            LOGGER.info("Opened database successfully");
            CreateDatabase.createTables(connection);
            LOGGER.info("Created database successfully");

        } catch (Exception e) {
            LOGGER.error("!!!Something went wrong: message = " + e.getMessage());
            System.exit(-1);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                LOGGER.error("!!!Something went wrong: message = " + e.getMessage());
            }
        }

    }

}
