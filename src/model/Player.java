package model;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Pia Kilian
 * Player is an object that represents the player of the game
 */
public class Player {
	/**
	 * Position of the Player on the game board
	 */
	private int boardPosition;
	/**
	 * Number of buttons
	 */
	private int money;
	/**
	 * Name of the Player
	 */
	private String name;
	/**
	 * The QuiltBoard from the player
	 */
	private QuiltBoard quiltBoard;
	/**
	 *The Player is from type easy, medium or hard
	 */
	private PlayerType playerType;
	/**
	 * The highscore from the player
	 */
	private Score score;

	/**
	 * Initializes a new Player-Object
	 * @param boardPosition position on the TimeBoard
	 * @param money number of buttons
	 * @param name name of the player
	 * @param playerType the Type of Player
	 */

	public Player( int boardPosition, int money, String name, PlayerType playerType ) {
		CheckUtil.assertNonNull(name);
		CheckUtil.assertNonNegative(money, boardPosition);

		if(name.isBlank()){
			throw new IllegalArgumentException("Player name is empty.");
		}
		this.boardPosition=boardPosition;
		this.money=money;
		this.name=name;
		this.playerType = playerType;

	}

	/**
	 * Clones the object
	 * @return Copy of the object
	 */
	@Override
	public Player clone() {
		return new Player(boardPosition, money, name, playerType);
	}

	/**
	 * Returns the BoardPosition
	 * @return boardPosition
	 */
	public int getBoardPosition() {
		return boardPosition;
	}

	/**
	 * Returns the PlayerType
	 * @return PlayerType
	 */
	public PlayerType getPlayerType() {
		return playerType;
	}
	/**
	 * Returns the money
	 * @return money
	 */
	public int getMoney(){
		return money;
	}
	/**
	 * Returns the name
	 * @return name
	 */
	public String getName(){
		return name;
	}
	/**
	 * Returns the score
	 * @return score
	 */
	public Score getScore(){
		return score;
	}
	/**
	 * Set score
	 * @param pScore
	 */
	public void setScore( Score pScore){ score = pScore;}
	/**
	 * Set the name
	 * @param pName
	 */
	public void setName( String pName){ name= pName;}
	/**
	 * Set boardPosition
	 * @param pBoardPosition
	 */
	public void setBoardPosition( int pBoardPosition){ boardPosition = pBoardPosition;}
	/**
	 * Set playerType
	 * @param pPlayerType
	 */
	public void setPlayerType( PlayerType pPlayerType){ playerType = pPlayerType;}
	/**
	 * Set quiltBoard
	 * @param pQuiltBoard
	 */
	public void setQuiltBoard( QuiltBoard pQuiltBoard){ quiltBoard = pQuiltBoard;}
	/**
	 * Checks for equality
	 * @param obj to compare to
	 * @return true if the objects are equal, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Player that = (Player) obj;
		return boardPosition == that.boardPosition &&
				money == that.money &&
				Objects.equals(name, that.name) &&
				Objects.equals(quiltBoard, that.quiltBoard) &&
				Objects.equals(playerType, that.playerType) &&
				Objects.equals(score, that.score) ;
	}
	/**
	 * Generates HashCode
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(boardPosition, money, name, playerType);
	}
}
