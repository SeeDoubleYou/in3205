package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import java.util.Stack;
import java.util.Vector;

import org.junit.Test;

/**
 * Series of test cases for the game itself.
 * It makes use of the GameTestCase fixture, which
 * contains a simple board.
 * @author Arie van Deursen, 2007
 * @version $Id: GameTest.java,v 1.10 2009/09/23 14:03:29 arie Exp $
 *
 */
public class GameTest extends GameTestCase {
    
    /**
     * Is the standard board created in the GameTestCase
     * setup correctly?
     */
    @Test
    public void testSetUp() {
        // Simply invoke the GameTestCase method conducting
        // the sanity check on the board created.
        checkBoardSanity();
    }

    /**
     * Is each list of monsters a fresh one?
     */
    @Test
    public void testGetMonsters() {
        assertEquals(2, getTheGame().getMonsters().size());
        // each call to getMonsters should deliver a fresh copy.
        Vector<Monster> ms1 = getTheGame().getMonsters();
        Vector<Monster> ms2 = getTheGame().getMonsters();
        assertNotSame(ms1, ms2);
    }

    /**
     * Are the dx/dy in the player correctly set after moving
     * the player around?
     */
    @Test
    public void testDxDyPossibleMove() {
        // start dx/dy should be zero.
        assertEquals(0, getTheGame().getPlayerLastDx());
        assertEquals(0, getTheGame().getPlayerLastDy());
        // move to left empty cell -- dx should have beeen adjusted.
        getTheGame().movePlayer(1, 0);
        assertEquals(1, getTheGame().getPlayerLastDx());
        assertEquals(0, getTheGame().getPlayerLastDy());
        // move to up empty cell -- dy should have been adjusted.
        getTheGame().movePlayer(0, -1);
        assertEquals(0, getTheGame().getPlayerLastDx());
        assertEquals(-1, getTheGame().getPlayerLastDy());
    }

    /**
     * Do the player dx/dy remain unaltered if a move fails?
     */
    @Test
    public void testDxDyImpossibleMove() {
        // start dx/dy should be zero.
        assertEquals(0, getTheGame().getPlayerLastDx());
        assertEquals(0, getTheGame().getPlayerLastDy());
        // move to a wallcell -- dxdy should have been adjusted.
        getTheGame().movePlayer(0, -1);
        assertEquals(0, getTheGame().getPlayerLastDx());
        assertEquals(-1, getTheGame().getPlayerLastDy());
    }
    
    /**
     * Test if the underlying stack in game that tracks moves is pushing
     * and popping moves in the correct order
     */
    @Test
    public void testUndoStack() {
    	PlayerMove move1 = new PlayerMove(getThePlayer(), getFoodCell());
    	PlayerMove move2 = new PlayerMove(getThePlayer(), getEmptyCell());
    	getTheGame().persistMove(move1);
    	getTheGame().persistMove(move2);
    	PlayerMove performed = (PlayerMove) getTheGame().getMostRecentMove();
    	assertEquals(performed, move2);
    	assertNotSame(performed, move1);
    	performed = (PlayerMove) getTheGame().getMostRecentMove();
    	assertEquals(performed, move1);
    }
}
