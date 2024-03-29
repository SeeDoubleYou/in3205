package jpacman.model;

import java.util.Stack;
import java.util.Vector;

/**
 * Representation of the board and its guests. This class's responsibilities
 * include the correct movement of moving guests, and keeping track of the state
 * of the game (whether the player died, or whether everything has been eaten,
 * for example)
 * <p>
 *
 * @author Arie van Deursen; Aug 24, 2003
 * @version $Id: Game.java,v 1.20 2009/02/03 19:42:45 arie Exp $
 */
public class Game {

	/**
	 * The stack of all moves done in the game.
	 */
	private Stack<Move> theStack = null;
	
    /**
     * The board containing all guests.
     */
    private Board theBoard = null;

    /**
     * The player of the game.
     */
    private Player thePlayer = null;

    /**
     * All monsters active in this game.
     */
    private Vector<Monster> monsters = null;

    /**
     * The total number of points that can be earned in this game.
     */
    private int totalPoints = 0;

    /**
     * The initial map / layout on the board.
     */
    private String[] theMap = null;


    /**
     * Create a new Game using a default map.
     */
    public Game() { }

    /**
     * Create a new Game using a custom map.
     * @param map The world to be used in the game.
     */
    public Game(String[] map) {
        theMap = map;
    }

    /**
     * Set the fields of the game to their initial values.     *
     * @throws GameLoadException if the game can't be loaded
     *         (in which case default values are used).
     */
    void initialize() throws GameLoadException {
    	theStack = new Stack<Move>();
        if (theMap == null) {
            try {
                theMap = (new GameLoader()).obtainMap();
            } catch (GameLoadException gle) {
                // switch to default world map
                // (which should always load correctly).
                theMap = GameLoader.DEFAULT_WORLD_MAP;
                loadWorld(theMap);
                // inform outside world of switch to new map.
                throw gle;
            }
        }
        loadWorld(theMap);
        assert invariant();
    }
    
    /**
     * Reset the game.
     */
    void reInitialize() {
        assert theMap != null;
        assert invariant();
        loadWorld(theMap);
        assert invariant();
    }

    /**
     * Check whether all relevant fields have been initialized.
     *
     * @return true iff initialization has completed successfully.
     */
    public boolean initialized() {
        return theBoard != null 
            && thePlayer != null
            && theStack != null
            && monsters != null
            && totalPoints >= 0;
    }

    /**
     * The game should always be in a consistent state,
     * in particular, a player cannot win and die at the same time.
     *
     * @return True iff the above holds.
     */
    private boolean consistent() {
        return !(playerDied() && playerWon())
        && thePlayer.getPointsEaten() <= totalPoints;
    }

    /**
     * A game is either not yet initialized, or it is in a consistent state.
     *
     * @return True iff this is the case.
     */
    protected boolean invariant() {
        return initialized() && consistent();
    }

    /**
     * Every move made in the game should be pushed onto the stack.
     * @param m The move to save
     */
    protected void persistMove(Move m) {
    	assert invariant();
    	assert m != null;
    	theStack.push(m);
    	assert invariant();
    }
    
    /**
     * The most recent move applied can be requested via this method.
     * pre-condition: we have at least one recent move saved
     * @return the most recent move applied
     */
    protected Move getMostRecentMove() {
    	assert invariant();
    	assert !theStack.isEmpty();
    	return theStack.pop();
    }
    
    /**
     * Have there been any moves done by either a monster or the player? 
     * @return true iff moves have been performed by some MovingGuest
     */
    protected boolean hasMoves() {
    	assert invariant();
    	return !theStack.isEmpty();
    }
    
    /**
     * We want to revive the player from his death.
     */
    protected void revive() {
    	assert invariant();
    	assert playerDied();
    	assert gameOver();
    	assert !playerWon();
    	getPlayer().live();
    	assert invariant();
    	assert !playerDied();
    	assert !gameOver();
    }
    
    /**
     * Get the board of this game, which can be null if
     * the game has not been initialized.
     *
     * @return The game's board.
     */
    public Board getBoard() {
        return theBoard;
    }

    /**
     * Precondition: the player doesn't exist yet.
     * @return a new Player.
     */
    private Player createPlayer() {
        assert thePlayer == null : "Player exists already";
        thePlayer = new Player();
        return thePlayer;
    }

    /**
     * @return a new food element.
     */
    private Food createFood() {
        Food f = new Food();
        totalPoints += f.getPoints();
        return f;
    }

    /**
     * Create monster, and add it to the list of known monsters.
     * @return a new monster.
     */
    private Monster createMonster() {
        Monster m = new Monster();
        monsters.add(m);
        return m;
    }

    /**
     * Return the current player, which can be null if the game has not yet been
     * initialized.
     *
     * @return the player of the game.
     */
    public Player getPlayer() {
        return thePlayer;
    }

    /**
     * Return a fresh Vector containing all the monsters in the game.
     *
     * @return All the monsters.
     */
    public Vector<Monster> getMonsters() {
        assert invariant();
        Vector<Monster> result = new Vector<Monster>();
        result.addAll(monsters);
        assert result != null;
        return result;
    }

    /**
     * Add a new guest to the board.
     * @param code Representation of the sort of guest
     * @param x x-position
     * @param y y-position
     */
    private void addGuestFromCode(char code, int x, int y) {
        assert getBoard() != null : "Board should exist";
        assert getBoard().withinBorders(x, y);
        Guest theGuest = null;
        switch (code) {
        case Guest.WALL_TYPE:
            theGuest = new Wall();
            break;
        case Guest.PLAYER_TYPE:
            theGuest = createPlayer();
            break;
        case Guest.FOOD_TYPE:
            theGuest = createFood();
            break;
        case Guest.MONSTER_TYPE:
            theGuest = createMonster();
            break;
        case Guest.EMPTY_TYPE:
            theGuest = null;
            break;
        default:
            assert false : "unknown cell type``" + code + "'' in worldmap";
        break;
        }
        assert (code == Guest.EMPTY_TYPE && theGuest == null) || theGuest != null;
        if (theGuest != null) {
            theGuest.occupy(getBoard().getCell(x, y));
        }
        assert theGuest == null
            || getBoard().getCell(x, y).equals(theGuest.getLocation());
    }

    /**
     * Load a custom map. Postcondition: the invariant holds.
     *
     * @param map
     *            String array for a customized world map.
     */
    private void loadWorld(String[] map) {
        assert map != null;
        assert GameLoader.checkSanity(map) == null;
        int height = map.length;
        assert height > 0 : "at least one cell with one player required.";
        int width = map[0].length();
        assert width > 0 : "empty rows not permitted.";
        
        // initialize Game fields.
        monsters = new Vector<Monster>();
        totalPoints = 0;
        thePlayer = null;
        theBoard = null;



        assert theBoard == null;
        theBoard = new Board(width, height);

        // read the map into the cells
        for (int y = 0; y < height; y++) {
            assert map[y].length() == width
                : "all lines in map should be of equal length.";
            for (int x = 0; x < width; x++) {
                assert getBoard().getGuest(x, y) == null
                    : "only empty cells can be filled.";
                addGuestFromCode(map[y].charAt(x), x, y);
            }
        }
        assert invariant();
    }

    /**
     * Move the player to offsets (x+dx,y+dy). If the move is not possible
     * (wall, beyond borders), the move is not carried out. Precondition:
     * initialized and game isn't over yet. Postcondition: if the move is
     * possible, it has been carried out, and the game has been updated to
     * reflect the new situation.
     *
     * @param dx
     *            Horizontal movement
     * @param dy
     *            Vertical movement
     */
    void movePlayer(int dx, int dy) {
        assert invariant();
        assert !gameOver() : "can only move when game isn't over";
        Cell targetCell =
            getPlayer().getLocation().cellAtOffset(dx, dy);
        PlayerMove playerMove = new PlayerMove(getPlayer(), targetCell);
        applyMove(playerMove);
        getPlayer().setLastDirection(dx, dy);
        assert invariant();
    }

    /**
     * Move a monster to offsets (x+dx,y+dy). If the move is not possible
     * (non empty cell, beyond borders), the move is not carried out.
     * Precondition: initialized and game is not over yet
     * Postcondition: if move possible, move was performed and game updated to
     * reflect the new situation
     * @param m
     * 		The monster to move
     * @param dx
     * 		Horizontal movement
     * @param dy
     * 		Vertical movement
     */
    void moveMonster(Monster m, int dx, int dy) {
    	assert invariant();
    	assert !gameOver();
    	assert m != null;
    	Cell targetCell = 
    		m.getLocation().cellAtOffset(dx, dy);
    	MonsterMove move = new MonsterMove(m, targetCell);
    	applyMove(move);
    	assert invariant();
    }

    /**
     * Actually apply the given move, if it is possible.
     * @param move The move to be made.
     */
    private void applyMove(Move move) {
        assert move != null;
        assert invariant();
        assert !gameOver();
        if (move.movePossible()) {
            move.apply();
            persistMove(move);
            assert move.moveDone();
            assert !playerDied() : "move possible => not killed";
        } else {
            if (move.playerDies()) {
                assert !playerWon() : "you can't win by dying";
                getPlayer().die();
                assert playerDied();
            }
        }
        assert invariant();
    }


    /**
     * Check if the player has died. Precondition: initialization completed.
     *
     * @return True iff the player is dead.
     */
    public boolean playerDied() {
        assert initialized();
        return !getPlayer().living();
    }

    /**
     * Check if the player has eaten all food. Precondition: initialization
     * completed.
     *
     * @return True iff the player has won.
     */
    public boolean playerWon() {
        assert initialized();
        return getPlayer().getPointsEaten() >= totalPoints;
    }

    /**
     * Check whether the game is over.
     *
     * @return True iff the game is over.
     */
    public boolean gameOver() {
        assert initialized();
        return playerWon() || playerDied();
    }

    /**
     * Return the delta in the x-direction of the last
     * (attempted or succesful) player move.
     * Returns 0 if player hasn't moved yet.
     * @return delta in x direction
     */
    public int getPlayerLastDx() {
        return getPlayer().getLastDx();
    }

    /**
     * Return the delta in the y-direction of the last
     * (attempted or successful) player move.
     * Returns 0 if player hasn't moved yet.
     * @return delta in y direction
     */
    public int getPlayerLastDy() {
        return getPlayer().getLastDy();
    }

    /**
     * Return the width of the Board used.
     * @return width of the board.
     */
    public int boardWidth() {
        return getBoard().getWidth();
    }

    /**
     * Return the height of the Board used.
     * @return height of the board.
     */
    public int boardHeight() {
        return getBoard().getHeight();
    }

    /**
     * Return the guest code at position (x,y).
     * @param x x-coordinate of guest
     * @param y y-coordinate of guest
     * @return Character representing guest at x,y
     */
    public char getGuestCode(int x, int y) {
        return getBoard().guestCode(x, y);
    }
}
