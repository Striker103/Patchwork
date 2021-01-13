package model;

public class Player {

	private int boardPosition;

	private int money;

	private String name;

	private QuiltBoard quiltBoard;

	private PlayerType playerType;

	private Score score;


	public Player(Integer boardPosition, Integer money, String name) {
	}


	@Override
	public Player clone() {
		return null;
	}

	public int getBoardPosition() {
		return boardPosition;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}
}
