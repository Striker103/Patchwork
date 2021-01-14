package model;

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
	 *  Saves the position of the time component
	 */
	private final int POSITION;


	/**
	 * Constructor for a new time board component
	 * @param positionOnBoard the position of the time board component
	 */
	public TimeBoardComponent(int positionOnBoard) {
		POSITION = positionOnBoard;
	}

	/**
	 * A method that clones the current time board component
	 * @return a copy of the time board component
	 */
	public TimeBoardComponent clone() {
		TimeBoardComponent timeBoardComponent = new TimeBoardComponent(POSITION);
		timeBoardComponent.setHasButton(hasButton);
		timeBoardComponent.setHasPatch(hasPatch);
		return timeBoardComponent;
	}

	/**
	 * Gets the current position of the time board component
	 * @return the current position
	 */
	public int getPosition(){
		return POSITION;
	}

	/**
	 * Checks the current component has a button
	 * @return true if the component has a button
	 */
	public boolean hasButton(){
		return hasButton;
	}

	/**
	 * Changes the value of hasButton
	 * @param buttonAvailable the new boolean value
	 */
	public void setHasButton(boolean buttonAvailable){
		hasButton = buttonAvailable;
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
	 * @param patchAvailable the new boolean value
	 */
	public void setHasPatch(boolean patchAvailable){
		hasPatch = patchAvailable;
	}

	/*
	 * Checks if 2 time board components are the same
	 * @param timeBoardComponent the second time board component
	 * @return true if the 2 components are the same

	public boolean equals(TimeBoardComponent timeBoardComponent){
		return this.POSITION == timeBoardComponent.POSITION;
	}
	*/
}
