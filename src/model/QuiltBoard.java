package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mohammed
 */
public class QuiltBoard {

	/**
	 * A 2D Array for the player's quilt board
	 */
	private Matrix patchBoard;

	/**
	 * A list of the patches on the quilt board
	 */
	private List<Patch> patches;

	/**
	 * Constructor for the class QuiltBoard
	 * Sets the size of the board to 9x9
	 */
	public QuiltBoard(){
		patchBoard = new Matrix(9,9);
		patches = new ArrayList<>();
	}

	/**
	 * Clones the quilt board
	 * @return A copy of the quilt board
	 */
	public QuiltBoard copy() {
		QuiltBoard cloneBoard = new QuiltBoard();
		for(Patch patch : this.patches){
			cloneBoard.getPatches().add(patch.copy());
		}
		cloneBoard.patchBoard = this.patchBoard.copy();
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
		patchBoard.insert(patch.getShape().multiply(patch.getPatchID()), posX, posY);
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
	public Matrix getPatchBoard(){
		return this.patchBoard;
	}

	/**
	 * Compares 2 QuiltBoard objects and returns if they are equal
	 * @param obj the other object
	 * @return true iff the 2 objects are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		QuiltBoard that = (QuiltBoard) obj;
		return this.patchBoard.equals(that.patchBoard) &&
				Objects.equals(patches, that.patches);
	}
}
