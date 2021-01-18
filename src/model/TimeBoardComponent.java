package model;


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
	 *  Saves the position of the time component
	 */
	private final int position;


	/**
	 * Constructor for a new time board component
	 * @param positionOnBoard the position of the time board component
	 */
	public TimeBoardComponent(int positionOnBoard)  {
		if (positionOnBoard<0){
			throw new IllegalArgumentException("the position can't be negative");
		}
		position = positionOnBoard;

	}

	/**
	 * A method that clones the current time board component
	 * @return a copy of the time board component
	 */
	public TimeBoardComponent copy() {
		TimeBoardComponent timeBoardComponent = new TimeBoardComponent(position);
		timeBoardComponent.setHasButton(hasButton);
		timeBoardComponent.setHasPatch(hasPatch);
		return timeBoardComponent;
	}

	/**
	 * Gets the current position of the time board component
	 * @return the current position
	 */
	public int getPosition(){
		return position;
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
	 *
	 * @param obj the other object
	 * @return true if the 2 components have the same position
	 */
	@Override
	public boolean equals(Object obj){
		if(this == obj) return true;
		if(obj == null || getClass() != obj.getClass()) return false;
		TimeBoardComponent tbc = (TimeBoardComponent) obj;
		return this.position == tbc.position;
	}

	private void setHasPatch(boolean patchAvailable ){
		hasPatch = patchAvailable;
	}

	private void setHasButton(boolean buttonAvailable ){
		hasButton = buttonAvailable;
	}
}
