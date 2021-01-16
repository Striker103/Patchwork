package model;

import java.util.Arrays;

/**
 * Represents a patch that is going to be placed on players quiltboard
 * @author Yannick
 */
public class Patch {

	/**
	 * X*X Patches
	 */
	private static final int MAXSIZEFORDIMENSION = 5;
	/**
	 * represents the income the player gets when moving over a button
	 */
	private final int buttonIncome;

	/**
	 * represents the cost to buy the patch
	 */
	private final int buttonsCost;

	/**
	 * a matrix where true represents that the tile is present on that block
	 */
	private final Matrix shape;

	/**
	 * the number of steps the player takes after buying the tile
	 */
	private final int time;

	/**
	 * intern identification of the patch
	 */
	private final int patchID;

	/**
	 * Instantiates a new Patch.
	 *
	 * @param patchID           number to identify the patch
	 * @param buttonIncome the income provided
	 * @param buttonsCost  the price to pay for the tile
	 * @param shape        the shape of the tile as a 5x5 array
	 * @param time         steps on the timeboard
	 *
	 * @throws IllegalArgumentException when there are null references or non natural numbers
	 */
	public Patch(int patchID, int buttonIncome, int buttonsCost, Matrix shape, int time) {
		CheckUtil.assertNonNull(patchID,buttonIncome,buttonsCost,shape,time);
		CheckUtil.assertNonNegative(buttonIncome,buttonsCost);
		CheckUtil.assertPositive(patchID,time);

		if(shape.getRows() != MAXSIZEFORDIMENSION|| shape.getColumns() != MAXSIZEFORDIMENSION) {
			throw new IllegalArgumentException("Shape has to be a 5x5 array");
		}

		this.patchID = patchID;
		this.buttonIncome = buttonIncome;
		this.buttonsCost = buttonsCost;
		this.shape = shape;
		this.time = time;

		if(this.shape.count(1)==0) throw new IllegalArgumentException("Patch has no shape defined");
	}


	public Patch copy() {
		return new Patch(this.patchID,this.buttonIncome,this.buttonsCost,this.shape.copy(),this.time);
	}

	/**
	 * Gets button income.
	 *
	 * @return income
	 */
	public int getButtonIncome() {
		return buttonIncome;
	}

	/**
	 * Gets buttons cost.
	 *
	 * @return cost of the patch
	 */
	public int getButtonsCost() {
		return buttonsCost;
	}

	/**
	 * Get the shape of the tile
	 *
	 * @return the shape
	 */
	public Matrix getShape() {
		return shape;
	}

	/**
	 * Gets the time of the patch
	 *
	 * @return tiles the player moves on the board
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Gets id.
	 *
	 * @return an intern id
	 */
	public int getPatchID() {
		return patchID;
	}

	/** Checks if two patches have the same id
	 *
	 * @param obj The potentially equal patch
	 * @return equality of two patches
	 */
	@Override
	public boolean equals(Object obj){
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Patch patch = (Patch) obj;
		return this.patchID == patch.patchID;
	}

}
