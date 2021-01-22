package model;

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
	private boolean hasSpecialTile;

	/**
	 * Creates a new Player with 5 money and the board position 0
	 * @param name the name of the player
	 * @param playerType the type of the player
	 */
	public Player(String name, PlayerType playerType) {
		CheckUtil.assertNonNull(name,playerType);
		if(name.isBlank()){
			throw new IllegalArgumentException("Player name is empty.");
		}
		this.boardPosition = 0;
		this.money = 5;
		this.name = name;
		this.playerType = playerType;
		this.quiltBoard = new QuiltBoard();
	}

	/**
	 * Copy Constructor
	 * @param boardPosition the boardPosition
	 * @param money the money
	 * @param name the name
	 * @param quiltBoard the quiltBoard
	 * @param playerType the playerType
	 * @param score the score
	 * @param hasSpecialTile the hasSpecialTile
	 */
	private Player(int boardPosition, int money, String name, QuiltBoard quiltBoard, PlayerType playerType, Score score, boolean hasSpecialTile) {
		this.boardPosition = boardPosition;
		this.money = money;
		this.name = name;
		this.quiltBoard = quiltBoard.copy();
		this.playerType = playerType;
		if(score != null){
			this.score = score.copy();
		}
		this.hasSpecialTile = hasSpecialTile;
	}

	/**
	 * Clones the object
	 * @return Copy of the object
	 */

	public Player copy() {
		return new Player(boardPosition,money,name,quiltBoard,playerType,score,hasSpecialTile);
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
	 * Creates the initilal Score for the Player
	 * @param isIronman whether or not the game is an ironman game
	 */
	public void createScore(boolean isIronman){
		score = new Score(0,isIronman,playerType,name);
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
	public void addMoney(int money){this.money = this.money + money;}

	/**
	 * sets money player money
	 * @param money the money the player has
	 */
	public void setMoney(int money){this.money =  money;}

	/**
	 * removes money player money
	 * @param money the money the player lost
	 */
	public void setMinusMoney(int money){this.money = this.money - money;}

	/**
	 * Set score
	 * @param pScore the new Score
	 */
	public void setScore( Score pScore){ score = pScore;}
	/**
	 * Set the name
	 * @param pName the new Name of Player
	 */
	public void setName( String pName){ name= pName;}
	/**
	 * Set boardPosition
	 * @param pBoardPosition the new board position
	 */
	public void setBoardPosition( int pBoardPosition){ boardPosition = pBoardPosition;}
	/**
	 * Set playerType
	 * @param pPlayerType new PlayerType
	 */
	public void setPlayerType( PlayerType pPlayerType){ playerType = pPlayerType;}
	/**
	 * Set quiltBoard
	 * @param pQuiltBoard new QuiltBoard
	 */
	public void setQuiltBoard( QuiltBoard pQuiltBoard){ quiltBoard = pQuiltBoard;}

	/**
	 * Set hasSpecialTile
	 * @param hasSpecialTile SpecialTile
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
