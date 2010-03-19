package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Specialize the general MoveTest test suite to one
 * that is tailored to MonsterMoves.
s */
public class MonsterMoveTest extends MoveTest {
    /**
     * Create a move object that will be tested.
     *  @see jpacman.model.MoveTest#createMove(jpacman.model.Cell)
     *  @param target The cell to be occupied by the move.
     *  @return The move to be tested.
     */
	@Override
	protected MonsterMove createMove(Cell target) {
		aMonsterMove = new MonsterMove(getThePlayer(), target);
        return aMonsterMove;
	}

	/*
	 * test that the player will in fact die when a monster meets a player.
	 */
	@Test
	public void playerDies()
	{
		Cell playerCell = getPlayerCell();
		Guest cellInhabitant = playerCell.getInhabitant()
		MonsterMove MonsterMove = createMove(playerCell);
		assertTrue(monsterMove.withinBorder());
		assertEquals(getThePlayer(), mosterMove.getPlayer());
		assertEquals(getThePlayer(), playerCell.getInhabitant());
		assertTrue(monsterMove.playerDies());
		assertTrue(monsterMove.invariant());
	}

	@Test
	public void allowMove()
	{
		Cell emptyCell = getEmptyCell();
		MonsterMove MonsterMove = createMove(emptyCell);
		assertTrue(monsterMove.withinBorder());
		assertTrue(monsterMove.movePossible());
		monsterMove.move();
		assertTrue(MonsterMove.moveDone());
		assertTrue(monsterMove.invariant());
	}
	
	@Test
	public void denyMoveWhenWall()
	{
		Cell wallCell = getWallCell();
		MonsterMove MonsterMove = createMove(wallCell);
		assertTrue(monsterMove.withinBorder());
		assertFalse(monsterMove.movePossible());
		assertTrue(monsterMove.invariant());
	}
	
	@Test
	public void denyMoveWhenMonster()
	{
		Cell monsterCell = getMonsterCell();
		MonsterMove MonsterMove = createMove(monsterCell);
		assertTrue(monsterMove.withinBorder());
		assertFalse(monsterMove.movePossible());
		assertTrue(monsterMove.invariant());
	}
}
