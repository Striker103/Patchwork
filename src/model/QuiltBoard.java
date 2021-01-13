package model;

import java.util.ArrayList;
import java.util.List;

public class QuiltBoard {

	/**
	 * A 2D Array for the player's quilt board
	 */
	private int[][] patchBoard;

	/**
	 * A list of the patches on the quilt board
	 */
	private List<Patch> patches;

	/**
	 * Constructor for the class QuiltBoard
	 * Sets the size of the board to 9x9
	 */
	public QuiltBoard(){
		patchBoard = new int[9][9];
		patches = new ArrayList<Patch>();
	}


	/**
	 * Clones the quilt board
	 * @return A copy of the quilt board
	 */
	@Override
	public QuiltBoard clone() {
		QuiltBoard clonedBoard = new QuiltBoard();
		clonedBoard.setPatches(this.patches);
		return clonedBoard;
	}


	/**
	 * Adds a new patch onto the quilt board
	 * @param patch The patch that will be added
	 */
	public void addPatch(Patch patch){
		patches.add(patch);
	}

	/**
	 * Getter for the list of patches on the quilt board
	 * @return the list of patches
	 */
	public List<Patch> getPatches(){
		return patches;
	}

	/**
	 * Setter for the list of patches on the quilt board
	 * @param patchList
	 */
	public void setPatches(List<Patch> patchList){
		this.patches = patchList;
	}
}
