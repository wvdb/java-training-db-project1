import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by wvdbrand on 18/09/2017.
 */
public class TestJunit {
    @Test
    public void dummyTestHappyFlow() {
        String str = "Junit is working fine";
        assertEquals("Junit is working fine",str);
    }

    @Ignore("ignored because this test will always fail")
    @Test
    public void dummyTestSadFlow() {
        String str = "Junit is working fine";
        assertEquals("Junit is working fine", str.toUpperCase());
    }
}
