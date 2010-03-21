package jpacman.model;

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
    	assertTrue(theEngine.inPlayingState());
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
    }
}
