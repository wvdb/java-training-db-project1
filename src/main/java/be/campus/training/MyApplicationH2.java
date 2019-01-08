package be.campus.training;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by wvdbrand on 14/09/2017.
 */
public class MyApplicationH2 {
    private static final Logger LOGGER = LogManager.getLogger(MyApplicationH2.class);

    private static final String SQL_1_BEER =
            "SELECT naam, alcohol FROM BIEREN ORDER BY naam ASC";

    private static final String SQL_6A_BEER =
                    " SELECT naam, alcohol " +
                    " FROM BIEREN " +
                    " WHERE alcohol BETWEEN ? AND ? " +
                    " ORDER BY alcohol ASC";

    private static final String SQL_6B_BEER =
            " SELECT naam, alcohol " +
                    " FROM BIEREN " +
                    " WHERE naam like ? " +
                    " ORDER BY naam ASC";

    private static final String SQL_6C_PLANT_BESTELLINGEN_BESTEL_DATUM_IN_RANGE =
            " SELECT bestelnr, b_datum, l_datum " +
                    " FROM BESTEL " +
                    " WHERE b_datum between ? and ?" +
                    " ORDER BY b_datum ASC";

    private static final String SQL_7_BEER =
            " SELECT count(*) " +
                    " FROM BIEREN ";

    private static final String SQL_8_BEER =
            " SELECT brouwernr, omzet " +
                    " FROM BROUWERS ";

    private static final String SQL_11_BEER =
            " INSERT into brouwers (brnaam, adres, postcode, gemeente, omzet, logo) " +
                    " VALUES ('Antwerpse Brouw Compagnie', 'Groenplaats 1', 2000, 'Antwerpen', 0, ?) ";

    public static final String JDBC_MYSQL_LOCALHOST_BIEREN = "jdbc:mysql://localhost/bieren";
    public static final String ROOT = "root";
    public static final String EMPTY_ROOT_PASSWORD = "";

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
                MyApplicationH2.oefeningBeers2();
                break;
            case 3:
                MyApplicationH2.oefeningBeers3();
                break;
            case 6:
                MyApplicationH2.oefeningBeers6();
                break;
            case 7:
                MyApplicationH2.oefeningTransactions7();
                break;
            case 8:
                MyApplicationH2.oefeningBeers8();
                break;
            case 11:
                MyApplicationH2 myApplicationMySQL = new MyApplicationH2();
                myApplicationMySQL.oefeningBeers11();
                break;
            case 99:
                MyApplicationH2.oefeningStoredProc99();
                break;
            default:
                LOGGER.error("Geen oefening voorzien");
        }

    }

    private static void oefeningBasic() {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:file:./bieren_h2.db");) {

            LOGGER.info("Opened connection to database successfully");
            CreateDatabase.createTables(connection);
            LOGGER.info("Created Tables successfully");

        } catch (Exception e) {
            LOGGER.error("!!!Error when creating Tables. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    private static void oefeningBeers2() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/bieren", ROOT, EMPTY_ROOT_PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(SQL_1_BEER) ) {
            logResultsetBeers2(resultSet);
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    private static void oefeningBeers3() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/bieren", ROOT, EMPTY_ROOT_PASSWORD);
             Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = stmt.executeQuery(SQL_1_BEER)) {
            logResultsetBeers3(resultSet);
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    private static void oefeningBeers6() {
        try (Connection connection = DriverManager.getConnection(JDBC_MYSQL_LOCALHOST_BIEREN, ROOT, EMPTY_ROOT_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(SQL_6A_BEER)) {
            stmt.setFloat(1, 3.0f);
            stmt.setFloat(2, 4.0f);
            ResultSet resultSet = stmt.executeQuery();
            logResultsetBeers2(resultSet);
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/bieren", ROOT, EMPTY_ROOT_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(SQL_6B_BEER)) {
            stmt.setString(1, "%tripel%");
            ResultSet resultSet = stmt.executeQuery();
            logResultsetBeers2(resultSet);
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/plantv", ROOT, EMPTY_ROOT_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(SQL_6C_PLANT_BESTELLINGEN_BESTEL_DATUM_IN_RANGE)) {
            stmt.setObject(1, LocalDate.of(1999, 3, 1));
            stmt.setObject(2, LocalDate.of(1999, 3, 31));
            ResultSet resultSet = stmt.executeQuery();
            logResultsetBestellingen(resultSet);
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }

    }

    private static void oefeningTransactions7() throws InterruptedException {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/bieren", ROOT, EMPTY_ROOT_PASSWORD);) {
            connection.setAutoCommit(false);

            // with this transaction-isolation we allow a dirty read
            // * delete has been done by second transaction but has not been committed
            // * still, the count(*) already takes into account the delete

            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

            // with this transaction-isolation we allow a dirty read
            // * delete has been done by second transaction but has not been committed
            // * the count(*) doesn't take into account the delete

//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

//            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            // let's sleep a little so we can modify the DB
            LOGGER.info("Ready to sleep");
            Thread.sleep(30_000);
            LOGGER.info("We slept well");

            PreparedStatement stmt = connection.prepareStatement(SQL_7_BEER);
            ResultSet resultSet = stmt.executeQuery();
            int numberOfBeers = 0;

            while (resultSet.next()) {
                numberOfBeers = resultSet.getInt(1);
            }
            LOGGER.info("Aantal bieren in bieren DB: {}.", numberOfBeers);

            stmt.close();
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    private static void oefeningBeers8() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/bieren", ROOT, EMPTY_ROOT_PASSWORD);
             Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE) ) {
            int numberOfBrouwersUpdated = 0;

            ResultSet resultSet = stmt.executeQuery(SQL_8_BEER);
            while (resultSet.next()) {
                Integer omzet = resultSet.getInt("omzet");
                omzet = resultSet.wasNull() ? null : omzet;

                if (omzet==null) {
                    omzet = 100;
                    resultSet.updateDouble("omzet", omzet);
                    numberOfBrouwersUpdated+=1;
                    resultSet.updateRow();
                } else if (omzet < 500) {
                    omzet = (int) (((int) omzet) * 1.1);
                    resultSet.updateDouble("omzet", omzet);
                    numberOfBrouwersUpdated+=1;
                    resultSet.updateRow();
                }

            }

            LOGGER.info("Aantal brouwers aangepast : {}.", numberOfBrouwersUpdated);

        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    private void oefeningBeers11() throws FileNotFoundException {
        // alter table brouwers add logo blob null;

        final String SEEFBIER_LOGO_LOCAAL_FILE = "C:\\wim\\oak3 - cronos- training\\cursus_data_input_output\\seefbier.jpg";
        final String SEEFBIER_LOGO_RESOURCE_FILE = "seefbier.jpg";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/bieren", ROOT, EMPTY_ROOT_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(SQL_11_BEER, Statement.RETURN_GENERATED_KEYS) ) {

            File file = new File(SEEFBIER_LOGO_LOCAAL_FILE);
            // werkt niet tgv spaces
//            ClassLoader classLoader = this.getClass().getClassLoader();
//            File file = new File(classLoader.getResource(SEEFBIER_LOGO_RESOURCE_FILE).getFile());

            FileInputStream fileInputStream = new FileInputStream(file);
            stmt.setBinaryStream(1, fileInputStream);

            stmt.executeUpdate();

            int brouwerNr = 0;
            try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                if (resultSet.next()) {
                   brouwerNr = resultSet.getInt(1);
                }
            }

            LOGGER.info("Antwerpen heeft een nieuwe brouwerij. Brouwerij nr = {}.", brouwerNr);
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    private static void oefeningStoredProc99() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/bieren", ROOT, EMPTY_ROOT_PASSWORD);
             CallableStatement stmt = connection.prepareCall("call myStoredProc1(14, 15)") ) {
            ResultSet resultSet = stmt.executeQuery();
            logResultsetBeers99(resultSet);
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }
    }

    private static void logResultsetBeers2(ResultSet resultSet) throws SQLException {
        int numRowsRetrieved=0;
        while (resultSet.next()) {
//            LOGGER.info("Bier: {}, {}, {}.", resultSet.getString("name"), resultSet.getDouble("alcohol"), resultSet.getDouble("price"));
//            LOGGER.info("Bier: {}, {}.", resultSet.getString(1), resultSet.getDouble(2));

            // our getters become nullSafe

            String naam = resultSet.getString(1);
            naam = resultSet.wasNull() ? "onbekend" : naam;

            Double alcohol = resultSet.getDouble(2);
            alcohol = resultSet.wasNull() ? null : alcohol;

            LOGGER.info("Bier: {}, {}.", naam, alcohol);

            numRowsRetrieved +=1;
        }
        LOGGER.info("Number of rows retrieved = {}.", numRowsRetrieved);
    }

    private static void logResultsetBeers3(ResultSet resultSet) throws SQLException {
        resultSet.last();
        while (resultSet.previous()) {
            LOGGER.info("Bier: {}, {}.", resultSet.getString(1), resultSet.getDouble(2));
        }
    }

    private static void logResultsetBestellingen(ResultSet resultSet) throws SQLException {
        int numRowsRetrieved=0;

        while (resultSet.next()) {
            String naam = resultSet.getString(1);
            LocalDate bestelDatum = resultSet.getDate(2).toLocalDate();
            LocalDate leverDatum = resultSet.getDate(3).toLocalDate();
            LOGGER.info("Bestelling: {}, {}, {}.", naam, bestelDatum, leverDatum);
            numRowsRetrieved +=1;
        }
        LOGGER.info("Number of bestellingen retrieved = {}.", numRowsRetrieved);
    }

    private static void logResultsetBeers99(ResultSet resultSet) throws SQLException {
        int numRowsRetrieved=0;
        while (resultSet.next()) {
            String naam = resultSet.getString("naam");
            naam = resultSet.wasNull() ? "onbekend" : naam;

            Double alcohol = resultSet.getDouble("alcohol");
            alcohol = resultSet.wasNull() ? null : alcohol;

            LOGGER.info("Bier: {}, {}.", naam, alcohol);

            numRowsRetrieved +=1;
        }
        LOGGER.info("Number of rows retrieved = {}.", numRowsRetrieved);
    }

}
