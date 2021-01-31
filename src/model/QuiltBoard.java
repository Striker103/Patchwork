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
	 * the placed patch and its Matrix
	 */
	private Tuple<Integer, Matrix> placedPatch;

	/**
	 * if the placed patch is flipped and its rotation
	 */
	private Tuple<Boolean, Integer> placedPatchOrientation;

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
	 * Places a patch onto the patch board
	 * @param patch the patch that should be placed
	 * @param placement the placement of the patch
	 * @param rotation the rotation angle of the patch
	 * @param flipped whether or not the patch is flipped
	 * @throws IllegalArgumentException if given an invalid rotation angle or if the patch overlaps with another patch
	 */
	public void addPatch(Patch patch, Matrix placement, int rotation, boolean flipped){
		int calculatedId = patch.getPatchID() ;
		if(rotation == 0 || rotation == 90 || rotation == 180 || rotation == 270){
			calculatedId += rotation;
		}
		else
			throw new IllegalArgumentException("Rotation angle not valid!");

		if(flipped)
			calculatedId *= -1;
		if(!patchBoard.disjunctive(placement)) throw new IllegalArgumentException("The patch does not fit in this position!");
		patchBoard.add(placement.multiply(calculatedId));
		patches.add(patch);
		placedPatch = new Tuple<>(patch.getPatchID(), placement);
		placedPatchOrientation = new Tuple<>(flipped, rotation);
	}

	/**
	 * Getter for the list of patches on the quilt board
	 * @return the list of patches
	 */
	public List<Patch> getPatches(){
		return patches;
	}

	/**
	 * Getter for Matrix shape and patch id
	 * @return tuple with elements
	 */
	public Tuple<Integer, Matrix> getPlacedPatch(){
		return placedPatch;
	}

	/**
	 *  Getter if patch is flipped and its rotation
	 * @return tuple with elements
	 */
	public Tuple<Boolean, Integer> getPlacedPatchOrientation(){
		return placedPatchOrientation;
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

	/**
	 * Adds the special patch onto the patch board
	 * @param posX the x-coordinate of the patch
	 * @param posY the y-coordinate of the patch
	 */
	public boolean add1x1Patch(int posX,int posY){
		if(patchBoard.get(posY,posX)!=0){
			return false;
		}
		patchBoard.set(posY,posX,Integer.MAX_VALUE);
		return true;
	}

	/**
	 * Is a 7x7 field filled
	 * @return whether or not the player should get the special tile
	 */
	public boolean check7x7(){
		return false;//TODO
	}

	/**
	 * Prints the PatchBoard
	 */
	public void print(){
		this.patchBoard.print();
	}
}
