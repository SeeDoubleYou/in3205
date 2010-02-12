package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
     * Width & height of board to be used.
     */
    private final int width = 4, height = 5;

    /**
     * The board the cells occur on.
     */
    private Board aBoard, differentBoard;

    /**
     * The "Cell Under Test".
     */
    private Cell boundaryCell, centerCell, cellA, cellB;

    /**
     * Actually create the board and the cells.
     */
    @Before
    public void setUpBoard() {
        aBoard = new Board(width, height);
        differentBoard = new Board(width, height);
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
     * A cell is adjacent to another cell if it lies directly next to 
     * another cell. We have four possible directions to check
     * Check if a cell that lies to the right, left, up or down. 
     * If so it should be adjacent
     */
    @Test
    public void testCellAdjacent() {
    	//test down adjacency
    	cellA = aBoard.getCell(2, 2);
    	cellB = aBoard.getCell(2, 3);
    	assertTrue(cellA.adjacent(cellB));
    	
    	//test up adjacency
    	cellB = aBoard.getCell(2, 1);
    	assertTrue(cellA.adjacent(cellB));
    	
    	//test left adjacency
    	cellB = aBoard.getCell(1, 2);
    	assertTrue(cellA.adjacent(cellB));
    	
    	//test right adjacency
    	cellB = aBoard.getCell(3, 2);
    	assertTrue(cellA.adjacent(cellB));
    	
    	//test if a cell is NOT adjacent
    	cellB = aBoard.getCell(3, 1);
    	assertFalse(cellA.adjacent(cellB));
    	
    	//test if a cell on another board is NOT adjacent
    	cellB = differentBoard.getCell(2, 3);
    	assertFalse(cellA.adjacent(cellB));
    }
     
}
