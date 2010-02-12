package jpacman.model;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static jpacman.model.GameLoader.checkSanity;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for loading games.
 * The main challenge lies in immitating failing IO behavior.s
 * 
 * @author Arie van Deursen
 * @version $Id$
 */
public class GameLoaderTest {
    

    /**
     * Test that the default board conforms to the sanity checks.
     */
    @Test
    public void correctBoard() {
        assertNull(checkSanity(GameLoader.DEFAULT_WORLD_MAP));
    }
    
    /**
     * An empty board is not OK.
     */
    @Test
    public void emptyBoard() {
        String[] map = new String[] {};
        assertNotNull(checkSanity(map));
    }
    
    /**
     * A board with too short rows is not OK.
     */
    @Test
    public void rowsTooShort() {
        String[] map = new String[] { "" };
        assertNotNull(checkSanity(map));
    }
    
    
    /**
     * A board with rows of different lengths is not OK.
     */
    @Test
    public void rowsDifferent() {
        String[] map = new String[] { "W", "WW" };
        assertNotNull(checkSanity(map));
    }

    /**
     * A board without a player is not ok.
     */
    @Test
    public void noPlayer() {
        String[] map = new String[] { "W", "W" };
        assertNotNull(checkSanity(map));
    }

    /**
     * A board with non-Guest characters is not OK.
     */
    @Test
    public void illegalChar() {
        String[] map = new String[] { "/" };
        assertNotNull(checkSanity(map));
    }

    
}
