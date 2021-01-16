package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Mohammed
 */
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
		QuiltBoard cloneBoard = new QuiltBoard();
		for(Patch patch : this.patches){
			cloneBoard.getPatches().add(patch.clone());
		}
		cloneBoard.patchBoard = this.patchBoard.clone();
		return cloneBoard;
	}

	/**
	 * Adds a patch onto the patch board, whereas (posX,posY) represents the top left
	 * field of the placement position on the patch board.
	 * @param patch the patch that should be added
	 * @param posX the x-coordinate of the placement position
	 * @param posY the y-coordinate of the placement position
	 */
	public void addPatch(Patch patch, int posX, int posY){
		int i = posX;
		int j;
		while(i < posX + 5 && i < patchBoard.length){
			j = posY;
			while(j < posY + 5 && j < patchBoard.length){
				if(patch.getShape()[i - posX][j - posY]){
					patchBoard[i][j] = patch.getPatchID();
				}
				j++;
			}
			i++;
		}
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
	 * Getter for the player's patch board
	 * @return the patch board
	 */
	public int[][] getPatchBoard(){
		return this.patchBoard;
	}

	/**
	 * Compares 2 QuiltBoard objects and returns if they are equal
	 * @param obj the other object
	 * @return true if the 2 objects are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		QuiltBoard that = (QuiltBoard) obj;
		return Arrays.equals(patchBoard, that.patchBoard) &&
				Objects.equals(patches, that.patches);
	}
}
