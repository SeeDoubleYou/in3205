package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import jpacman.TestUtils;

import org.junit.Test;

/**
 * Specialize the general MoveTest test suite to one
 * that is tailored to MonsterMoves.
s */
public class MonsterMoveTest extends MoveTest {
	
	/**
     * The move the monster would like to make.
     */
	private MonsterMove aMonsterMove;
	
    /**
     * Create a move object that will be tested.
     *  @see jpacman.model.MoveTest#createMove(jpacman.model.Cell)
     *  @param target The cell to be occupied by the move.
     *  @return The move to be tested.
     */
	@Override
	protected MonsterMove createMove(Cell target) {
		aMonsterMove = new MonsterMove(getTheMonster(), target);
        return aMonsterMove;
	}

	/*
	 * test that the player will in fact die when a monster meets a player.
	 */
	@Test
	public void playerDies() {
		Cell playerCell = getPlayerCell();
		MonsterMove monsterMove = createMove(playerCell);
		assertEquals(getThePlayer(), playerCell.getInhabitant());
		assertEquals(getTheMonster(), monsterMove.getMonster());
		assertTrue(monsterMove.playerDies());
		assertTrue(monsterMove.invariant());
	}

	@Test
	public void allowMove() {
		Cell emptyCell = getEmptyCell();
		MonsterMove monsterMove = createMove(emptyCell);
		assertTrue(monsterMove.movePossible());
		monsterMove.apply();
		assertTrue(monsterMove.moveDone());
		assertTrue(monsterMove.invariant());
	}
	
	@Test
	public void denyMoveWhenWall() {
		Cell wallCell = getWallCell();
		MonsterMove monsterMove = createMove(wallCell);
		assertFalse(monsterMove.movePossible());
		assertTrue(monsterMove.invariant());
	}
	
	@Test
	public void denyMoveWhenMonster() {
		Cell monsterCell = getMonsterCell();
		MonsterMove monsterMove = createMove(monsterCell);
		assertFalse(monsterMove.movePossible());
		assertTrue(monsterMove.invariant());
	}
	
	@Test
	/**
	 * Test a move by a monster to an empty cell
	 * the monster should move to the cell, undo the move
	 * the monster should have returned to its original cell
	 */
	public void testUndoMonsterMove() {
		Cell emptyCell = getEmptyCell();
		aMonsterMove = createMove(emptyCell);
		assertTrue(aMonsterMove.movePossible());
		Cell original = aMonsterMove.getMonster().getLocation();
		assertNotSame(original, emptyCell);
		aMonsterMove.apply();
		assertEquals(emptyCell, aMonsterMove.getMonster().getLocation());
		aMonsterMove.undo();
		assertEquals(original, aMonsterMove.getMonster().getLocation());
	}
	
	@Test
    /**
     * Test moving a monster to a cell outside the borders of the board
     */
    public void testMovingOutsideBorders() {
    	if (TestUtils.assertionsEnabled()) {
    		boolean failureGenerated;
    		boolean movePossible = false;
    		try {    			
    			Board board = getTheGame().getBoard();
    			Cell cellOutside = board.getCell(board.getWidth() + 1, board.getHeight() + 1);
    			MonsterMove move = createMove(cellOutside);
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
}
