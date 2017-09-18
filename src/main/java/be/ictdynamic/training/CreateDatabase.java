package be.ictdynamic.training;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by wvdbrand on 18/09/2017.
 */
public class CreateDatabase {
    private static final Logger LOGGER = Logger.getLogger(CreateDatabase.class);

    static void createTables(Connection connection) {
        String sql = "CREATE TABLE IF NOT EXISTS EMPLOYEE"  +
                "(ID INT PRIMARY KEY            NOT NULL,"  +
                " NAME           VARCHAR(50)    NOT NULL, " +
                " AGE            INT            NOT NULL, " +
                " HIREDATE       DATE,                    " +
                " ADDRESS        CHAR(50),                " +
                " SALARY         REAL)";

        createTableImpl(connection, sql);

        sql = "CREATE TABLE IF NOT EXISTS DEPARTMENT"  +
                "(ID INT PRIMARY KEY            NOT NULL,"  +
                " NAME           VARCHAR(50)    NOT NULL, " +
                " ADDRESS        CHAR(50))";

        createTableImpl(connection, sql);

        sql = "CREATE TABLE IF NOT EXISTS EMPLOYEE_DEPARTMENT"  +
                "(ID             INT PRIMARY KEY            NOT NULL,"  +
                " EMPLOYEE_ID    INT                        NOT NULL, " +
                " DEPT_ID        INT                        NOT NULL) ";

        createTableImpl(connection, sql);
    }

    static void createTableImpl(Connection connection, String sql) {
        Statement stmt;

        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();

            LOGGER.info("Table has been created successfully");
        } catch (SQLException e) {
            LOGGER.error("!!!Something went wrong: message = " + e.getMessage());
        }

    }

}
