package be.campus.training;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by wvdbrand on 18/09/2017.
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
                "(ID INT PRIMARY KEY            NOT NULL,"  +
                " NAME           VARCHAR(50)    NOT NULL, " +
                " ADDRESS        CHAR(50))";

        createTableImpl(connection, sql);

        sql = "CREATE TABLE IF NOT EXISTS EMPLOYEE_DEPARTMENT"  +
                "(ID             INT PRIMARY KEY            NOT NULL,"  +
                " EMPLOYEE_ID    INT                        NOT NULL, " +
                " DEPT_ID        INT                        NOT NULL) ";

        createTableImpl(connection, sql);

        sql = "CREATE TABLE IF NOT EXISTS BIEREN2"  +
                 " (BierNr INT PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
                 "  Naam  VARCHAR(100), " +
                 "  BrouwerNr  INT, " +
                 "  PoortNr  INT, " +
                 "  Alcohol  DOUBLE " +
                " ) ";

        createTableImpl(connection, sql);
    }

    private static void createTableImpl(Connection connection, String sql) throws SQLException {
            Statement stmt = connection.createStatement();
            int dummyReturnValue = stmt.executeUpdate(sql);
            stmt.close();

    }

}
