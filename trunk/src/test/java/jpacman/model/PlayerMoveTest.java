package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import jpacman.TestUtils;

import org.junit.Test;

/**
 * Specialize the general MoveTest test suite to one
 * that is tailored to PlayerMoves.
 * Thanks to inheritance, all test cases from MoveTest
 * are also methods in PlayerMoveTest, thus helping us
 * to test conformance with Liskov's Substitution Principle (LSP)
 * of the Move hierarchy.
 * <p>
 * @author Arie van Deursen; August 21, 2003.
 * @version $Id: PlayerMoveTest.java,v 1.8 2008/02/10 19:51:11 arie Exp $
 */
public class PlayerMoveTest extends MoveTest {

    /**
     * The move the player would like to make.
     */
    private PlayerMove aPlayerMove;

    /**
     * Simple test of a few getters.
     */
    @Test
    public void testSimpleGetters() {
        PlayerMove playerMove = new PlayerMove(getThePlayer(), getFoodCell());
        assertEquals(getThePlayer(), playerMove.getPlayer());
        assertTrue(playerMove.movePossible());
        assertFalse(playerMove.playerDies());
        assertEquals(1, playerMove.getFoodEaten());
        assertEquals(getTheFood(), getFoodCell().getInhabitant());
        assertTrue(playerMove.invariant());
    }

    @Test
    /**
     * Test moving a player to a cell outside the borders of the board
     */
    public void testMovingOutsideBorders() {
    	if (TestUtils.assertionsEnabled()) {
    		boolean failureGenerated;
    		boolean movePossible = false;
    		try {    			
    			Board board = getTheGame().getBoard();
    			Cell cellOutside = board.getCell(board.getWidth() + 1, board.getHeight() + 1);
    			PlayerMove move = createMove(cellOutside);
    			movePossible = move.movePossible();
    			failureGenerated = false;
    		}
    		catch (AssertionError ae) {
    			failureGenerated = true;
    		}
    		
    		assertTrue(failureGenerated);
    		assertFalse(movePossible);
    	}
    }
    
    @Test
    /*
     * A move to a wall should not be possible
     */
    public void testMoveToWall() {
    	PlayerMove move = createMove(getWallCell());
    	assertFalse(move.movePossible());
    	assertFalse(move.playerDies());
    	assertTrue(move.invariant());
    }
    
    @Test
    /*
     * A move to a monster should kill me
     */
    public void testMoveToMonster() {
    	PlayerMove move = createMove(getMonsterCell());
    	assertFalse(move.movePossible());
    	assertTrue(move.playerDies());
    	assertTrue(move.invariant());
    }
    
    @Test
    /*
     * A move to an empty cell should be ok
     */
    public void testMoveToEmptyCell() {
    	PlayerMove move = createMove(getEmptyCell());
    	assertTrue(move.movePossible());
    	assertFalse(move.playerDies());
    	move.apply();
    	assertTrue(move.moveDone());
    	assertTrue(move.invariant());
    }
    
    @Test
    /*
     * Test that eating food should increase my score
     */
    public void testEatingFood() {
    	PlayerMove move = createMove(getFoodCell());
    	int food = getThePlayer().getPointsEaten();
    	assertEquals(0, food);
    	assertTrue(move.movePossible());
    	assertEquals(1, move.getFoodEaten());
    	move.apply();
    	assertTrue(move.moveDone());
    	assertEquals(food + 1, getThePlayer().getPointsEaten());
    	assertTrue(move.invariant());
    }
    
    @Test
    /*
     * Test winning the game by eating all food
     */
    public void testWinTheGame() {
    	Cell theOtherFoodCell = getTheGame().getBoard().getCell(0, 2);
    	boolean isFood = theOtherFoodCell.getInhabitant() instanceof Food;
    	assertTrue(isFood);
    	PlayerMove move = createMove(getTheGame().getBoard().getCell(0, 2));
    	assertTrue(move.movePossible());
    	move.apply();
    	assertTrue(move.moveDone());
    	assertTrue(move.invariant());
    	move = createMove(getFoodCell());
    	assertTrue(move.movePossible());
    	move.apply();
    	assertTrue(move.moveDone());
    	assertTrue(getTheGame().gameOver());
    	assertTrue(getTheGame().playerWon());
    	assertFalse(getTheGame().playerDied());
    	assertTrue(move.invariant());
    }
    
    /**
     * test undo move when food was eaten
     */
    @Test public void testFoodUndo() {
    	PlayerMove move = createMove(getFoodCell());
    	int food1 = getThePlayer().getPointsEaten();
    	assertTrue(move.movePossible());
    	move.apply();
    	int food2 = food1 + 1;
    	assertEquals(food2, getThePlayer().getPointsEaten());
    	move.undo();
    	assertEquals(food1, getThePlayer().getPointsEaten());
    }
        
    /**
     * Create a move object that will be tested.
     *  @see jpacman.model.MoveTest#createMove(jpacman.model.Cell)
     *  @param target The cell to be occupied by the move.
     *  @return The move to be tested.
     */
    @Override
    protected PlayerMove createMove(Cell target) {
        aPlayerMove = new PlayerMove(getThePlayer(), target);
        return aPlayerMove;
    }
}
