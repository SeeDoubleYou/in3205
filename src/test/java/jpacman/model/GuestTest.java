package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import jpacman.TestUtils;

import org.junit.Before;
import org.junit.Test;


/**
 * Test the Cell Guest association.
 * Originally set up following Binder's Class Association Test
 * pattern.
 *
 * @author Arie van Deursen; Jul 29, 2003
 * @version $Id: GuestTest.java,v 1.7 2009/01/31 19:55:03 arie Exp $
 */
public class GuestTest {

    /**
     * Two cells that we'll (try to) occupy.
     */
    private Cell theCell, anotherCell;

    /**
     * Two guests that will participate in occupying cells.
     */
    private Guest theGuest, anotherGuest;

    /**
     * Create two cells, and two guests, which we can
     * use for occupying / deoccupying different cells with various guests.
     */
    @Before
    public void setUp() {
        // create the board.
        int width = 2;
        int height = 2;
        Board theBoard = new Board(width, height);

        // obtain two cells.
        int x = 0;
        int y = 0;
        theCell = theBoard.getCell(x, y);
        anotherCell = theBoard.getCell(x + 1, y);

        // create two guests.
        theGuest = new Food();
        anotherGuest = new Food();
    }
    
    /**
     * Minimal tests for some basic properties.
     */
    @Test
    public void testSimpleProperties() {
        assertFalse(theCell.equals(anotherCell));
        assertFalse(theGuest.equals(anotherGuest));
        assertEquals(Guest.FOOD_TYPE, theGuest.guestType());
        assertNull(theGuest.getLocation());
    }

    /**
     * Test occupy and deoccupy for Guest cell occupation
     */
    @Test
    public void testOccupyDeoccupy() {
    	theGuest.occupy(theCell);
    	assertTrue(theGuest.getLocation().equals(theCell));
    	assertTrue(theCell.getInhabitant().equals(theGuest));
    	assertFalse(theGuest.getLocation() == null);
    	assertFalse(theCell.getInhabitant() == null);
    	
    	theGuest.deoccupy();
    	assertTrue(theGuest.getLocation() == null);
    	assertTrue(theCell.getInhabitant() == null);
    	assertNull(theGuest.getLocation());
    	assertNull(theCell.getInhabitant());
    }
    
    /**
     * Test an invalid occupy-occupy sequence
     */
    @Test
    public void testDoubleOccupy() {
    	theGuest.occupy(theCell);
    	boolean failureGenerated;
    	if (TestUtils.assertionsEnabled()) {
    		try {
    			theGuest.occupy(theCell);
    			failureGenerated = false;
    		}
    		catch (AssertionError ae) {
    			failureGenerated = true;
    		}
    		
    		assertTrue(failureGenerated);    		
    	}
    }
    
    /**
     * 
     *  Test illegal use of Cell.setGuest()
     */
    @Test
    public void testIllegalsetGuest() {
    	boolean failureGenerated;
    	if (TestUtils.assertionsEnabled()) {
    		try {
    			anotherCell.setGuest(anotherGuest);
    			failureGenerated = false;
    		}
    		catch (AssertionError ae) {
    			failureGenerated = true;
    		}
    		
    		assertTrue(failureGenerated);
    	}    	
    }
}
