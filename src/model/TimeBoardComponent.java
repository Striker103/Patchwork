package model;


import java.util.Arrays;

/**
 * @author Maya Samha
 */
public class TimeBoardComponent {

	/**
	 *  Returns whether the current time board component has a Button or not
	 */
	private boolean hasButton;

	/**
	 *  Returns whether the current time board component has a Patch or not
	 */
	private boolean hasPatch;

	/**
	 * the positions of the buttons on the time board
	 */
	private final int[] buttonPositions = {5, 11, 17, 23, 29, 35, 41, 47, 53};

	/**
	 * the position of the patches on the time board
	 */
	private int[] patchPositions = {26 ,32, 38, 44, 50};

	/**
	 * Constructor for a new time board component
	 * @param positionOnBoard the position of the component on the time board
	 * @throws IllegalArgumentException if a negative position is given
	 */
	public TimeBoardComponent(int positionOnBoard)  {
		if (positionOnBoard<0){
			throw new IllegalArgumentException("the position can't be negative");
		}

		for(int buttonPos : buttonPositions){
			if(buttonPos == positionOnBoard) {
				hasButton = true;
				break;
			}
		}

		for(int patchPos : patchPositions){
			if(patchPos == positionOnBoard) {
				hasPatch = true;
				break;
			}
		}

	}

	/**
	 *  Constructor for a new time board component
	 * @param hasButton gives whether there is a  button on the component or not
	 * @param hasPatch gives whether there is a  patch on the component or not
	 */
	public TimeBoardComponent(boolean hasButton, boolean hasPatch)  {
		this.hasButton= hasButton;
		this.hasPatch= hasPatch;

	}

	/**
	 * A method that clones the current time board component
	 * @return a copy of the time board component
	 */
	public TimeBoardComponent copy() {
		return new TimeBoardComponent(hasButton, hasPatch);
	}

	/**
	 * Checks the current component has a button
	 * @return true if the component has a button
	 */
	public boolean hasButton(){
		return hasButton;
	}

	/**
	 * Checks the current component has a patch
	 * @return true if the component has a patch
	 */
	public boolean hasPatch(){
		return hasPatch;
	}

	/**
	 * Changes the value of hasPatch
	 */
	public void removePatch(){
		hasPatch = false;
	}

	/**
	 * Checks the equality of 2 TimeBoardComponent objects
	 * @param obj the other object
	 * @return true if the 2 components have the same position
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}

		if (obj.getClass() == this.getClass())
		{
			TimeBoardComponent timeBoardComponent = (TimeBoardComponent) obj;
			return hasButton == timeBoardComponent.hasButton && hasPatch== timeBoardComponent.hasPatch;

		}
		return false;
	}

}
