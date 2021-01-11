package model;

public class Game {

	private int currentGameState;

	private boolean highscoreReachable;

	private final boolean ironman;

	private int simulationSpeed;

	private GameState[] gameState;

	public Game(boolean ironman) {
		this.ironman = ironman;
	}

	public void addGameState(GameState gameState) {

	}

	@Override
	public Game clone() {
		return null;
	}

}
