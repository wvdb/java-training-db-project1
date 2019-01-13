package be.campus.training;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

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

    private static final String SQL_INSERT_PROJECT =
            " INSERT into PROJECT (ID, NAME, START_DATE) " +
                    " VALUES (?, ?, ?) ";

    private static final String SQL_SELECT_PROJECT =
            " select ID, NAME, START_DATE from PROJECT";

    private static final String SQL_INSERT_BROUWER =
            " INSERT into BROUWER_ACERTA (Naam, Locatie) " +
                    " VALUES ('InBev Belgium', 'Brouwerijplein 1, 3000 Leuven') ";

    private static final String SQL_SELECT_BIER =
            " SELECT Naam, Alcohol, BrouwerNr from BIER_ACERTA";

    private static final String SQL_UPDATE_BIER =
            " UPDATE BIER_ACERTA set BrouwerNr = 1";

    private static final String SQL_6A_BEER =
            " SELECT naam, alcohol, BrouwerNr  " +
                    " FROM BIER_ACERTA " +
                    " WHERE alcohol BETWEEN ? AND ? " +
                    " ORDER BY alcohol ASC";


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
//                MyApplicationH2.oefeningInsertProject();
                break;
            case 3:
                MyApplicationH2.oefening3SelectBeer();
                break;
            case 4:
                MyApplicationH2.oefening4SelectBeerResulsetReversed();
                break;
            case 5:
                MyApplicationH2.oefening5UpdateBieren();
                break;
            case 6:
                MyApplicationH2.oefening6PrepareStatement();
                break;
            case 8:
                MyApplicationH2.oefening8BatchUpdate();
                break;
            case 99:
                MyApplicationH2.oefening99AProjectUUID();
                MyApplicationH2.oefening99BProjectUUID();
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

    private static void oefeningInsertProject() {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:file:./bieren_training_h2.db");
             PreparedStatement stmt = connection.prepareStatement(SQL_INSERT_PROJECT, Statement.RETURN_GENERATED_KEYS) ) {

            stmt.executeUpdate();

            int brouwerNr = 0;
            try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                if (resultSet.next()) {
                    brouwerNr = resultSet.getInt(1);
                }
            }

            LOGGER.info("Leuven heeft een brouwerij. Brouwerij nr = {}.", brouwerNr);
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    private static void oefening3SelectBeer() {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:file:./bieren_training_h2.db");
            Statement stmt = connection.createStatement() ) {
            ResultSet resultSet = stmt.executeQuery(SQL_SELECT_BIER);
            logResultsetBeers(resultSet);
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    private static void oefening4SelectBeerResulsetReversed() {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:file:./bieren_training_h2.db");
            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY) ) {
            ResultSet resultSet = stmt.executeQuery(SQL_SELECT_BIER);
            logResultsetBeersReversedOrder(resultSet);
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    private static void oefening5UpdateBieren() {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:file:./bieren_training_h2.db");
            Statement stmt = connection.createStatement() ) {
            int result = stmt.executeUpdate(SQL_UPDATE_BIER);
            LOGGER.info("{} beers have been updated.", result);
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    private static void oefening6PrepareStatement() {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:file:./bieren_training_h2.db");
            PreparedStatement stmt = connection.prepareStatement(SQL_6A_BEER)) {
            stmt.setFloat(1, 3.0F);
            stmt.setFloat(2, 5.0F);
            ResultSet resultSet = stmt.executeQuery();
            logResultsetBeers(resultSet);
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    private static void oefening8BatchUpdate() {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:file:./bieren_training_h2.db");
            Statement stmt = connection.createStatement()) {
            stmt.addBatch(SQL_UPDATE_BIER);
            stmt.addBatch(SQL_UPDATE_BIER);
            int[] results = stmt.executeBatch();
            for (int result : results) {
                System.out.println("Aantal gewijzigde rijen = " + result);
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    private static void oefening99AProjectUUID() {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:file:./bieren_training_h2.db");
            PreparedStatement stmt = connection.prepareStatement(SQL_INSERT_PROJECT)) {
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, "Project 1");
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    private static void oefening99BProjectUUID() {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:file:./bieren_training_h2.db");
             PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_PROJECT)) {
            ResultSet resultSet = stmt.executeQuery();
            logResultsetProjects(resultSet);
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    private static void logResultsetBeers(ResultSet resultSet) throws SQLException {
        int numRowsRetrieved=0;
        while (resultSet.next()) {
            String naam = resultSet.getString(1);
            naam = resultSet.wasNull() ? "onbekend" : naam;

            double alcohol = resultSet.getDouble(2);
            alcohol = resultSet.wasNull() ? null : alcohol;

            int brouwerNr = resultSet.getInt(3);
            brouwerNr = resultSet.wasNull() ? 0 : brouwerNr;

            LOGGER.info("Bier: naam:{}, alcohol:{}, brouwerNr.", naam, alcohol, brouwerNr);

            numRowsRetrieved +=1;
        }
        LOGGER.info("Number of rows retrieved = {}.", numRowsRetrieved);
    }

    private static void logResultsetBeersReversedOrder(ResultSet resultSet) throws SQLException {
        int numRowsRetrieved=0;
        resultSet.last();
        do {
            String naam = resultSet.getString(1);
            naam = resultSet.wasNull() ? "onbekend" : naam;

            Double alcohol = resultSet.getDouble(2);
            alcohol = resultSet.wasNull() ? null : alcohol;

            LOGGER.info("Bier: naam:{}, alcohol:{}.", naam, alcohol);

            numRowsRetrieved +=1;
        } while (resultSet.previous());
        LOGGER.info("Number of rows retrieved = {}.", numRowsRetrieved);
    }

    private static void logResultsetProjects(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            LOGGER.info("Project: id: {}, naam: {}, startDate: {}.", resultSet.getString(1), resultSet.getString(2), resultSet.getDate(3));
        }
    }

}
