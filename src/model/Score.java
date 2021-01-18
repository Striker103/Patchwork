package model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Alexandra Latys
 * Score is an object that represents a score one player achieved during one game
 */
public class Score{

	/**
	 * Calculated Score
	 */
	private int value;

	/**
	 * Date the score was achieved at
	 */
	private LocalDateTime date;

	/**
	 * Game mode
	 */
	private final boolean isIronman;

	/**
	 * Type of opponent
	 */
	private final PlayerType opponentType;

	/**
	 * Name of the player
	 */
	private final String playerName;

	/**
	 * Initializes a new Score-Object
	 * @param value Number of points
	 * @param isIronman Game mode
	 * @param opponentType Type of Opponent
	 * @param playerName Name of the player
	 * @throws IllegalArgumentException If invalid parameters are provided
	 */
	public Score(int value, boolean isIronman, PlayerType opponentType, String playerName) {

		CheckUtil.assertNonNull(opponentType, playerName);

		if(playerName.isBlank())
			throw new IllegalArgumentException("Player name is empty.");


		this.value = value;
		this.date = LocalDateTime.now();
		this.isIronman = isIronman;
		this.opponentType = opponentType;
		this.playerName = playerName;
	}

	/**
	 * Clones the object
	 * @return Copy of the object
	 */
	public Score copy() {
		//String is immutable
		return new Score(value, isIronman, opponentType, playerName);
	}

	/**
	 * Returns the actual score
	 * @return number of points
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Returns date
	 * @return date
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * Returns the game mode
	 * @return isIronman
	 */
	public boolean isIronman() {
		return isIronman;
	}

	/**
	 * Returns type of the opponent
	 * @return type of opponent
	 */
	public PlayerType getOpponentType() {
		return opponentType;
	}

	/**
	 * Returns the player name
	 * @return player name
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Set score value
	 * @param value value
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Set score date
	 * @param date date
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	/**
	 * Checks for equality
	 * @param obj to compare to
	 * @return true if the objects are equal, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Score score = (Score) obj;
		return value == score.value &&
				isIronman == score.isIronman &&
				Objects.equals(date, score.date) &&
				opponentType == score.opponentType &&
				playerName.equals(score.playerName);
	}


	/**
	 * Generates HashCode
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(value, date, isIronman, opponentType, playerName);
	}

}