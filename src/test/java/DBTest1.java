import be.campus.training.MyApplicationH2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by wvdbrand on 13/01/2019.
 */
public class DBTest1 {
    private static final Logger LOGGER = LogManager.getLogger(DBTest1.class);

    @Test
    public void dummyTestHappyFlow() {
        // given - my database is up and running and my database has been created

        // when
        MyApplicationH2.oefeningInsertBeer(MyApplicationH2.SQL_INSERT_BIER_STELLA);

        //then
        try (Connection connection = DriverManager.getConnection("jdbc:h2:file:./bieren_training_h2.db");
            Statement stmt = connection.createStatement() ) {
            ResultSet resultSet = stmt.executeQuery("SELECT Naam, Alcohol from BIER_ACERTA where BierNr = (select min(BierNr) from BIER_ACERTA)");
            while (resultSet.next()) {
                assertEquals("validating column 1", "Stella", resultSet.getString(1));
                assertEquals("validating column 2", new Double(4.5), new Double(resultSet.getDouble(2)));
            }
        } catch (SQLException e) {
            LOGGER.error("!!!Error when executing sql. Exception = {}, message = {}.", e, e.getMessage());
            System.exit(-1);
        }

    }

}
