package model;

import java.util.List;

public class Game {

	private int currentGameState;

	private boolean highscoreReachable;

	private final boolean ironman;

	private int simulationSpeed;

	private List<GameState> gameState;

	public Game(boolean ironman) {
		this.ironman = ironman;
	}

	public void addGameState(GameState gameState) {

	}

	@Override
	public Game clone() {
		return null;
	}

	public int getCurrentGameState() {
		return currentGameState;
	}

	public List<GameState> getGameStates() {
		return gameState;
	}
}
