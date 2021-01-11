package model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Alexandra Latys
 * Score is an immutable object that represents a score one player achieved during one game
 */
public class Score implements Comparable<Score>{

	/**
	 * Calculated Score
	 */
	private final int value;

	/**
	 * Date the score was achieved at
	 */
	private final LocalDateTime date;

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

		if(value < 0)
			throw new IllegalArgumentException("Score cannot be less than 0.");

		this.value = value;
		this.date = LocalDateTime.now();
		this.isIronman = isIronman;
		this.opponentType = opponentType;
		this.playerName = playerName;
	}

	/** Clones the object
	 * @return Copy of the object
	 */
	@Override
	public Score clone() {
		//String is immutable
		return new Score(value, isIronman, opponentType, playerName);
	}

	/** Returns the actual score
	 * @return number of points
	 */
	public int getValue() {
		return value;
	}

	/** Returns date
	 * @return date
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/** Returns the game mode
	 * @return isIronman
	 */
	public boolean isIronman() {
		return isIronman;
	}

	/** Returns type of the opponent
	 * @return type of opponent
	 */
	public PlayerType getOpponentType() {
		return opponentType;
	}

	/** Returns the player name
	 * @return player name
	 */
	public String getPlayerName() {
		return playerName;
	}

	/** Checks for equality
	 * @param obj to compare to
	 * @return true if the objects are equal, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Score score = (Score) obj;
		return value == score.value;
	}

	/** Generates HashCode
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	/** Compares this score to another score
	 * @param other other score
	 * @return {@link Comparable#compareTo(Object)}
	 */
	@Override
	public int compareTo(Score other) {
		return value - other.value;
	}
}