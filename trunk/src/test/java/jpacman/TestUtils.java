package jpacman;

/**
 * Utility methods that help in testing.
 *
 * @author Arie van Deursen, TU Delft, 2006
 * @version $Id: TestUtils.java,v 1.7 2009/02/24 08:46:26 arie Exp $
 */

public final class TestUtils {

    /**
     * Utility class, no constructor.
     */
    private TestUtils() { }
    
    /**
     * A helper method that just tells us whether assertions are enabled
     * or not.
     * @return True iff assertions are enabled.
     */
    public static boolean assertionsEnabled() {
        boolean hasAssertEnabled = false;
        //notice the intentional side effect!
        assert hasAssertEnabled = true;
        return hasAssertEnabled;
        // Alternative: 
        // return (new Object()).getClass().desiredAssertionStatus();
    }
}
