package model;

import java.util.List;

public class GameState {

	private boolean specialTileAvailable;

	private String logEntry;

	private List<TimeBoardComponent> timeBoard;

	private List<Patch> patch;

	private Player player1;

	private Player player2;

	public Player getCurrentPlayer() {
		return null;
	}

	@Override
	public GameState clone() {
		return null;
	}

}
