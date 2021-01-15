package model;

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
	 * @param playerType
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

	public int getMoney(){
		return money;
	}

	public String getName(){
		return name;
	}

	public QuiltBoard getQuiltBoard(){
		return quiltBoard;
	}

	public Score getScore(){
		return score;
	}
}
