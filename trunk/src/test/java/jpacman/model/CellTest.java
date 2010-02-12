package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for methods working directly on Cells.
 *
 * @author Arie van Deursen; Jul 29, 2003
 * @version $Id: CellTest.java,v 1.23 2010/01/22 12:30:55 arie Exp $
 */
public class CellTest {

    /**
     * Width & heigth of board to be used.
     */
    private final int width = 4, height = 5;

    /**
     * The board the cells occur on.
     */
    private Board aBoard;

    /**
     * The "Cell Under Test".
     */
    private Cell boundaryCell, centerCell;

    /**
     * Actually create the board and the cells.
     */
    @Before
    public void setUpBoard() {
        aBoard = new Board(width, height);
        // put the cell on a "risky" invariant boundary value.
        boundaryCell = aBoard.getCell(0, height - 1);
        centerCell = aBoard.getCell(1, 1);
    }



    /**
     * Test obtaining a cell at a given offset. Ensure both postconditions
     * (null value if beyond border, value with board) are executed.
     */
    @Test
    public void testCenterCellAtOffset() {
        assertEquals(boundaryCell, centerCell.cellAtOffset(-1, height - 2));
	 // TODO: add test cases out of the borders.
        assertEquals(boundaryCell, centerCell.cellAtOffset(-1, height - 2));
    }

    /**
     * Test if a cell is adjacent to another cell
     */
    @Test
    public void testCellAdjacent() {
    	//test down adjacency
    	boundaryCell = aBoard.getCell(2, 2);
    	centerCell = aBoard.getCell(2, 3);
    	assertTrue(boundaryCell.adjacent(centerCell));
    	
    	//test up adjacency
    	centerCell = aBoard.getCell(2, 1);
    	assertTrue(boundaryCell.adjacent(centerCell));
    	
    	//test left
    	
    }
     
}
