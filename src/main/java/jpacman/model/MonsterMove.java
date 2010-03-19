package jpacman.model;

/**
 * Class to represent the effects of moving a monster.
 */
public class MonsterMove extends Move {

	/**
     * The player wishing to move.
     */
    private Monster theMonster;
    
    /**
     * Create a move for the given monster to a given target cell.
     *
     * @param monster
     *            the monster to be moved
     * @param newCell
     *            the target location.
     * @see jpacman.model.Move
     */
    public MonsterMove(Monster monster, Cell newCell) {
        // preconditions checked in super method,
        // and cannot be repeated here ("super(...)" must be 1st stat.).
        super(monster, newCell);
        theMonster = monster;
        precomputeEffects();
        assert invariant();
    }
    
    /**
     * Verify that the monster/mover equal
     * and non-null.
     *
     * @return true iff the invariant holds.
     */
    public boolean invariant() {
        return moveInvariant() && theMonster != null
        	   && getMovingGuest().equals(theMonster);
    }
    
    /**
     * Attempt to move the moster towards a target guest.
     * @param targetGuest The guest that the monster will meet.
     * @return false at all times, since the monsters cannor move over accoupied cells
     * @see jpacman.model.Move#tryMoveToGuest(jpacman.model.Guest)
     */
    @Override
    protected boolean tryMoveToGuest(Guest targetGuest) {
        assert tryMoveToGuestPrecondition(targetGuest)
            : "percolated precondition";
        if(targetGuest.guestType() == Guest.PLAYER_TYPE)
        {
        	die();
        }
        return false;
    }
    
    /**
     * Return the monster initiating this move.
     *
     * @return The moving monster.
     */
    public Monster getMonster() {
        assert invariant();
        return theMonster;
    }
}
