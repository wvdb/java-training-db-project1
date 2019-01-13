package be.campus.training;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by wvdbrand on 14/09/2017.
 */
public class MyApplicationH2 {
    private static final Logger LOGGER = LogManager.getLogger(MyApplicationH2.class);

    public static final String SQL_INSERT_BIER_STELLA =
            " INSERT into BIER_ACERTA (Naam, Alcohol) " +
                    " VALUES ('Stella', 4.5) ";

    private static final String SQL_INSERT_BIER_LEFFE_BLOND =
            " INSERT into BIER_ACERTA (Naam, Alcohol) " +
                    " VALUES ('Leffe blond', 7.0) ";

    private static final String SQL_INSERT_BIER_LEFFE_BRUIN =
            " INSERT into BIER_ACERTA (Naam, Alcohol) " +
                    " VALUES ('Leffe bruin', 7.0) ";

    public static void main(String args[]) throws InterruptedException, FileNotFoundException {
        int oefeningInteger = Integer.MIN_VALUE;

        while (oefeningInteger <= Integer.MIN_VALUE) {
            System.out.println("Enter identifier of the exercise: ");
            Scanner reader = new Scanner(System.in);
            try {
                oefeningInteger = reader.nextInt();
            }
            catch (InputMismatchException | NumberFormatException e) {
                LOGGER.error("Invalid value. Try again. Exception message = " + e.getMessage());
                return;
            }
        }

        switch (oefeningInteger) {
            case 1:
                MyApplicationH2.oefeningBasic();
                break;
            case 2:
                MyApplicationH2.oefeningInsertBeer(SQL_INSERT_BIER_STELLA);
                MyApplicationH2.oefeningInsertBeer(SQL_INSERT_BIER_LEFFE_BLOND);
                MyApplicationH2.oefeningInsertBeer(SQL_INSERT_BIER_LEFFE_BRUIN);
                break;
            default:
                LOGGER.error("Geen oefening voorzien");
        }

    }

    private static void oefeningBasic() {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:file:./bieren_training_h2.db");) {

            LOGGER.info("Opened connection to database successfully");
            CreateDatabase.createTables(connection);
            LOGGER.info("Created Tables successfully");

        } catch (SQLException e) {
            LOGGER.error("!!!Error when creating Tables. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    public static void oefeningInsertBeer(String insertStatement) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:file:./bieren_training_h2.db");
            PreparedStatement stmt = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS) ) {

            stmt.executeUpdate();

            int bierNr = 0;
            try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                if (resultSet.next()) {
                   bierNr = resultSet.getInt(1);
                   LOGGER.info("Beer with id {} has been inserted.", bierNr);
                }
            }

        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

}
