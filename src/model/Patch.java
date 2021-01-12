package model;

/**
 * Represents a patch that is going to be placed on players quiltboard
 * @author Yannick
 */
public class Patch {

	/**
	 * X*X Patches
	 */
	private static final int MAXSIZEFORDIMENSION = 4;
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
	private final boolean[][] shape;

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
	 * @param shape        the shape of the tile as a 4x4 array
	 * @param time         steps on the timeboard
	 *
	 * @throws IllegalArgumentException when there are null references or non natural numbers
	 */
	public Patch(int patchID, int buttonIncome, int buttonsCost, boolean[][] shape, int time) {
		CheckUtil.assertNonNull(patchID,buttonIncome,buttonsCost,shape,time);
		CheckUtil.assertNonNegative(buttonIncome);
		CheckUtil.assertPositive(patchID,time,buttonsCost);

		if(shape.length != MAXSIZEFORDIMENSION) {
			throw new IllegalArgumentException("Shape has to be a 4x4 array");
		}

		for(boolean[] arr : shape){
			if(arr.length != MAXSIZEFORDIMENSION)
				throw new IllegalArgumentException("Shape has to be a 4x4 array");
		}


		if(!checkShape(shape)){
			throw new IllegalArgumentException("No shape of the patch defined");
		}

		this.patchID = patchID;
		this.buttonIncome = buttonIncome;
		this.buttonsCost = buttonsCost;
		this.shape = shape;
		this.time = time;
	}


	@Override
	public Patch clone() {
		return new Patch(this.patchID,this.buttonIncome,this.buttonsCost,this.shape.clone(),this.time);
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
	public boolean[][] getShape() {
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


	private boolean checkShape(boolean[][] shape) {
		boolean anyTrue = false;
		for(boolean[] boolArr : shape){
			for(boolean value : boolArr){
				anyTrue = anyTrue ||value;
			}
		}
		return anyTrue;
	}

}
