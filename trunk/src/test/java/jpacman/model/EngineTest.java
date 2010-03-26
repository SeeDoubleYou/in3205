package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


/**
 * Systematic testing of the game state transitions.
 *
 * The test makes use of the simple map and its containing monsters
 * and players, as defined in the GameTestCase.
 * <p>
 *
 * @author Arie van Deursen; Aug 5, 2003
 * @version $Id: EngineTest.java,v 1.12 2009/09/20 09:37:56 arie Exp $
 */
public class EngineTest extends GameTestCase {

    /**
     * The engine that we'll push along every possible transition.
     */
    private Engine theEngine;


    /**
     * Set up an Engine, making use of the Game object
     * (with a small map containing all sorts of guests)
     * created in the superclass.
     * @throws GameLoadException if game resources can't be found.
     */
    @Before public void setUp() throws GameLoadException {
        theEngine = new Engine(getTheGame());
        theEngine.initialize();
        assertTrue(theEngine.inStartingState());
    }

     // Create state model test cases.
    /**
     * Test Case 1: starting-playing-playing-playerwon-starting 
     * as obtained from UML state diagram
     */
    @Test
    public void testStateTransition1() {    	
    	assertTrue(theEngine.inStartingState());
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());    
    	theEngine.movePlayer(-1, 0); //eat food left 
    	assertTrue(theEngine.inPlayingState());
    	theEngine.movePlayer(0, 1); //eat food down 
    	assertTrue(theEngine.inGameOverState());
    	assertTrue(theEngine.inWonState());
    	theEngine.start();
    	assertTrue(theEngine.inStartingState());
    	assertTrue(theEngine.invariant());
    }
    
    /**
     * Test Case 2: starting-playing-halted-playing
     * as obtained from UML state diagram
     */
    @Test
    public void testStateTransition2() {
    	assertTrue(theEngine.inStartingState());
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	theEngine.quit();
    	assertTrue(theEngine.inHaltedState());
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	assertTrue(theEngine.invariant());
    }
    
    /**
     * Test Case 3: starting-playing-playing-playerdied
     * as obtained from UML state diagram
     */
    @Test
    public void testStateTransition3() {
    	assertTrue(theEngine.inStartingState());
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	theEngine.movePlayer(1, 0); //go to the empty cell to the right
    	assertTrue(theEngine.inPlayingState());
    	theEngine.movePlayer(-1, 1); //go to a monster
    	assertTrue(theEngine.inGameOverState());
    	assertTrue(theEngine.inDiedState());
    	assertTrue(theEngine.invariant());
    }
    
    /**
     * Test Case 4: starting-playing-playing-playerdied-starting
     * as obtained from UML state diagram
     */
    @Test
    public void testStateTransition4() {
    	assertTrue(theEngine.inStartingState());
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	theEngine.moveMonster(getTheMonster(), 1, 0); //monster goes to empty cell
    	assertTrue(theEngine.inPlayingState());
    	theEngine.moveMonster(getTheMonster(), -1, -1); //monster goes to player
    	assertTrue(theEngine.inGameOverState());
    	assertTrue(theEngine.inDiedState());
    	theEngine.start();
    	assertTrue(theEngine.inStartingState());
    	assertTrue(theEngine.invariant());
    }
    
    /**
     * Test Sneak Path: starting-halted-starting
     */
    @Test
    public void testSneakPath1() {
    	assertTrue(theEngine.inStartingState());
    	theEngine.quit();
    	assertFalse(theEngine.inHaltedState());
    	
    	//go to playing then halted
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	theEngine.quit();
    	assertTrue(theEngine.inHaltedState());
    	
    	//now go from halted to starting
    	theEngine.start();
    	assertFalse(theEngine.inStartingState());
    	assertTrue(theEngine.invariant());
    }
    
    /**
     * Test Sneak Path: starting-gameover-playing
     */
    @Test
    public void testSneakPath2() {
    	assertTrue(theEngine.inStartingState());
    	theEngine.movePlayer(0, 1); //move player to monster and die
    	assertFalse(theEngine.inGameOverState());
    	
    	//go to playing, kill player then go to playing again
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	theEngine.movePlayer(0, 1); //move player to monster and die
    	assertTrue(theEngine.inGameOverState());
    	theEngine.start();
    	assertFalse(theEngine.inPlayingState());
    	assertTrue(theEngine.invariant());
    }
    
    /**
     * Test Sneak Path: starting-playing-starting
     */
    @Test
    public void testSneakPath3() {
    	assertTrue(theEngine.inStartingState());
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	theEngine.start();
    	assertFalse(theEngine.inStartingState());
    	assertTrue(theEngine.invariant());
    }
    
    /**
     * Test Sneak Path: starting-halted-gameover-halted
     */
    @Test
    public void testSneakPath4() {
    	assertTrue(theEngine.inStartingState());
    	theEngine.quit();
    	assertFalse(theEngine.inHaltedState());
    	
    	//go to playing then halted
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	theEngine.quit();
    	assertTrue(theEngine.inHaltedState());
    	
    	//go to gameover from halted
    	theEngine.movePlayer(0, 1); //move player to monster and die
    	assertFalse(theEngine.inGameOverState());
    	
    	//go to halted from gameover
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	theEngine.movePlayer(0, 1); //move player to monster and die
    	assertTrue(theEngine.inGameOverState());
    	theEngine.quit();
    	assertFalse(theEngine.inHaltedState());
    	assertTrue(theEngine.invariant());
    }
    
    /**
     * Test undo simple player move
     */
    @Test
    public void testUndoPlayerMove() {
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	int x = getThePlayer().getLocation().getX();
    	int y = getThePlayer().getLocation().getY();
    	theEngine.movePlayer(1, 1); //move to empty cell next to me
    	assertEquals(x+1, getThePlayer().getLocation().getX());
    	assertEquals(y+1, getThePlayer().getLocation().getY());
    	theEngine.undo(); //undo last move    	
    	assertEquals(x, getThePlayer().getLocation().getX());
    	assertEquals(y, getThePlayer().getLocation().getY());
    	assertTrue(theEngine.inHaltedState());
    }
    
    /**
     * Test undoing after quitting
     */
    @Test
    public void testUndoAfterQuit() {
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	theEngine.quit();
    	assertTrue(theEngine.inHaltedState());
    	theEngine.undo();
    	assertTrue(theEngine.inHaltedState());
    }
    
    /**
     * Test undo after player dies by meeting monster
     */
    @Test
    public void testUndoDieMove() {
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	theEngine.movePlayer(0,1);
    	assertFalse(getThePlayer().living());
    	assertTrue(theEngine.inGameOverState());
    	assertTrue(getTheGame().playerDied());
    	theEngine.undo();
    	assertTrue(getThePlayer().living());
    	assertFalse(getTheGame().playerDied());
    	assertTrue(theEngine.inHaltedState());
    	assertTrue(theEngine.invariant());
    }   
    
    /**
     * Test undo after monster kills player
     */
    @Test
    public void testUndoAfterMonsterDie() {
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	theEngine.moveMonster(getTheMonster(), 0, -1);
    	assertFalse(getThePlayer().living());
    	assertTrue(theEngine.inGameOverState());
    	assertTrue(getTheGame().playerDied());
    	theEngine.undo();
    	assertTrue(theEngine.inHaltedState());
    	assertFalse(getTheGame().playerDied());
    	assertTrue(getThePlayer().living());
    	assertTrue(theEngine.invariant());
    }
    
    /**
     * Test undo when no moves have been done
     */
    @Test
    public void testUndoNotAllowed() {
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	theEngine.undo();
    	assertFalse(theEngine.inHaltedState()); //no moves done yet, so cant undo anything
    }
    
    /**
     * Test many monster moves undone
     */
    @Test
    public void testUndoManyMonsterMoves() {
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	int x = getThePlayer().getLocation().getX();
    	int y = getThePlayer().getLocation().getY();
    	theEngine.movePlayer(1, -1);
    	assertEquals(x+1, getThePlayer().getLocation().getX());
    	assertEquals(y-1, getThePlayer().getLocation().getY());
    	int mx = getTheMonster().getLocation().getX();
    	int my = getTheMonster().getLocation().getY();
    	theEngine.moveMonster(getTheMonster(), 1, 0);
    	theEngine.moveMonster(getTheMonster(), 0, -1);
    	theEngine.moveMonster(getTheMonster(), -1, 0);
    	theEngine.undo();
    	assertTrue(theEngine.inHaltedState());
    	assertTrue(getThePlayer().living());
    	assertEquals(x, getThePlayer().getLocation().getX());
    	assertEquals(y, getThePlayer().getLocation().getY());
    	assertEquals(mx, getTheMonster().getLocation().getX());
    	assertEquals(my, getTheMonster().getLocation().getY());
    }
    
    /**
     * Test undo score
     */
    @Test
    public void testUndoScoring() {
    	theEngine.start();
    	assertTrue(theEngine.inPlayingState());
    	int score = getThePlayer().getPointsEaten();
    	theEngine.movePlayer(-1, 0); //eat some food
    	assertEquals(score+1, getThePlayer().getPointsEaten());
    	theEngine.undo();
    	assertEquals(score, getThePlayer().getPointsEaten());
    }
}
