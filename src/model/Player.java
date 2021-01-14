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
	 */
	public Player(Integer boardPosition, Integer money, String name) {
		CheckUtil.assertNonNull(name);
		CheckUtil.assertNonNegative(money, boardPosition);

		if(name.isBlank()){
			throw new IllegalArgumentException("Player name is empty.");
		}
		boardPosition.this=boardPosition;
		money.this=money;
		name.this=name;

	}


	@Override
	public Player clone() {
		return new Player(boardPosition, money, name);
	}

	/**
	 * Returns the BoardPosition
	 * @return
	 */
	public int getBoardPosition() {
		return boardPosition;
	}

	/**
	 * Returns the PlayerType
	 * @return
	 */
	public PlayerType getPlayerType() {
		return playerType;
	}
}
