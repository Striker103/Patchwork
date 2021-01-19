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
	 * Indicates whether the player receives 7 bonus points
	 */
	private boolean hasSpecialTile = false;

	/**
	 * Initializes a new Player-Object
	 * @param boardPosition position on the TimeBoard
	 * @param money number of buttons
	 * @param name name of the player
	 * @param playerType the Type of Player
	 */

	public Player( int boardPosition, int money, String name, PlayerType playerType, boolean hasSpecialTile) {
		CheckUtil.assertNonNull(name);
		CheckUtil.assertNonNegative(money, boardPosition);

		if(name.isBlank()){
			throw new IllegalArgumentException("Player name is empty.");
		}
		this.boardPosition=boardPosition;
		this.money=money;
		this.name=name;
		this.playerType = playerType;
		this.hasSpecialTile = hasSpecialTile;

	}

	/**
	 * Clones the object
	 * @return Copy of the object
	 */

	public Player copy() {
		return new Player(boardPosition, money, name, playerType, hasSpecialTile);
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
	public QuiltBoard getQuiltBoard () {
		return quiltBoard;
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
	 * Returns the whether the player has the special tile
	 * @return has special tile
	 */
	public boolean getHasSpecialTile() {
		return hasSpecialTile;
	}

	/**
	 * adds money player money
	 * @param money the money the player got
	 */
	public void setMoney(int money){this.money = this.money + money;}

	/**
	 * removes money player money
	 * @param money the money the player lost
	 */
	public void setMinusMoney(int money){this.money = this.money - money;}

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
	 * Set hasSpecialTile
	 * @param hasSpecialTile
	 */
	public void setHasSpecialTile(boolean hasSpecialTile) {
		this.hasSpecialTile = hasSpecialTile;
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
		Player that = (Player) obj;
		return boardPosition == that.boardPosition &&
				money == that.money &&
				hasSpecialTile == that.hasSpecialTile &&
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
		return Objects.hash(boardPosition, money, name, playerType, hasSpecialTile);
	}
}
