import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by wvdbrand on 18/09/2017.
 */
public class BasicTest {
    private static final Logger LOGGER = LogManager.getLogger(BasicTest.class);

    @Test
    public void dummyTestHappyFlow() {
        String str = "Junit is working fine";
        assertEquals("this is a dummy Test", "Junit is working fine",str);
    }

//    @Ignore("ignored because this test will always fail")
    @Test
    public void dummyTestSadFlow() {
        String str = "Junit is working fine";
        assertEquals("Junit is working fine", str.toUpperCase());
    }
}
