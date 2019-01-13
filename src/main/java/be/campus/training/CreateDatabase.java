package be.campus.training;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by wvdbrand on 06/01/2019.
 */
public class CreateDatabase {
    private static final Logger LOGGER = LogManager.getLogger(CreateDatabase.class);

    static void createTables(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS EMPLOYEE"  +
                "(ID INT PRIMARY KEY            NOT NULL,"  +
                " NAME           VARCHAR(50)    NOT NULL, " +
                " AGE            INT            NOT NULL, " +
                " HIREDATE       DATE,                    " +
                " ADDRESS        CHAR(50),                " +
                " SALARY         REAL)";

        createTableImpl(connection, sql);

        sql = "CREATE TABLE IF NOT EXISTS DEPARTMENT"  +
                "(ID             INT PRIMARY KEY            NOT NULL,"  +
                " NAME           VARCHAR(50)                NOT NULL, " +
                " ADDRESS        CHAR(50))";

        createTableImpl(connection, sql);

        sql = "CREATE TABLE IF NOT EXISTS EMPLOYEE_DEPARTMENT"  +
                "(ID             INT PRIMARY KEY            NOT NULL,"  +
                " EMPLOYEE_ID    INT                        NOT NULL, " +
                " DEPT_ID        INT                        NOT NULL) ";

        createTableImpl(connection, sql);

        sql = "CREATE TABLE IF NOT EXISTS PROJECT"  +
                "(ID             VARCHAR(50) PRIMARY KEY    NOT NULL, " +
                " NAME           VARCHAR(50)                NOT NULL, " +
                " START_DATE     DATE                       NOT NULL, " +
                " END_DATE       DATE                                )";

        createTableImpl(connection, sql);

        sql = "CREATE TABLE IF NOT EXISTS BIER_ACERTA"  +
                 " (BierNr      INT PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
                 "  Naam        VARCHAR(100)                NOT NULL , " +
                 "  BrouwerNr   INT, " +
                 "  Alcohol     DOUBLE                      NOT NULL   " +
                " ) ";

        createTableImpl(connection, sql);

        sql = "CREATE TABLE IF NOT EXISTS BROUWER_ACERTA"  +
                " (BrouwerNr   INT PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
                "  Naam        VARCHAR(100), " +
                "  Locatie     VARCHAR(100) " +
                " ) ";

        createTableImpl(connection, sql);
    }

    private static void createTableImpl(Connection connection, String sql) throws SQLException {
            Statement stmt = connection.createStatement();
            int dummyReturnValue = stmt.executeUpdate(sql);
            stmt.close();
    }

}
