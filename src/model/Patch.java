package model;

/**
 * Represents a patch that is going to be placed on players quiltboard
 * @author Yannick
 */
public class Patch {


	/**
	 * represents the income the player gets when moving over a button
	 */
	private final int buttonIncome;

	/**
	 * represents the cost to buy the patch
	 */
	private final int buttonsCost;

	/**
	 * a matrix where true represents that the tile is present on thath block
	 */
	private final boolean[][] shape;

	/**
	 * the number of steps the player takes after buying the tile
	 */
	private final int time;

	/**
	 * intern identification of the patch
	 */
	private final int id;

	/**
	 * Instantiates a new Patch.
	 *
	 * @param id           number to identify the patch
	 * @param buttonIncome the income provided
	 * @param buttonsCost  the price to pay for the tile
	 * @param shape        the shape of the tile as a 4x4 array
	 * @param time         steps on the timeboard
	 *
	 * @throws IllegalArgumentException when there are null references or non natural numbers
	 */
	public Patch(int id,int buttonIncome, int buttonsCost, boolean[][] shape, int time) {
		CheckUtil.assertNonNull(id,buttonIncome,buttonsCost,shape,time);
		CheckUtil.assertNonNegative(id,buttonIncome,buttonsCost,time);

		if(shape.length != 4)
			throw new IllegalArgumentException("Shape has to be a 4x4 array");

		for(boolean[] arr : shape){
			if(arr.length != 4)
				throw new IllegalArgumentException("Shape has to be a 4x4 array");
		}

		this.id = id;
		this.buttonIncome = buttonIncome;
		this.buttonsCost = buttonsCost;
		this.shape = shape;
		this.time = time;
	}
	@Override
	public Patch clone() {
		return (this.clone());
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
	public int getId() {
		return id;
	}

}
