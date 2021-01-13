package model;

/**
 * @author Abdullah Ourfali
 * Represents the Game
 */
public class Game {
	/**
	 * Currently state of the game
	 */
	private int currentGameState;

	/**
	 * The highscore is reachable
	 */
	private boolean highscoreReachable;

	/**
	 * Is ironman mode
	 */
	private final boolean ironman;

	/**
	 * The simulated speed
	 */
	private int simulationSpeed;

	/**
	 * All states of the game
	 */
	private GameState[] gameState;

	/**
	 *Initializes a new Game
	 * @param ironman the mode of the new game
	 */
	public Game(boolean ironman) {
		this.ironman = ironman;
	}

	/**
	 * Gets the currently state of the game
	 * @return the currently state
	 */
	public int getCurrentGameState(){
		return currentGameState;
	}

	/**
	 * Gets the reachabled highscore
	 * @return the reachabled highscore
	 */
	public boolean getHighscoreReachable(){
		return highscoreReachable;
	}

	/**
	 * Gets the game mode
	 * @return the ironman mode
	 */
	public boolean getIronman(){
		return ironman;
	}

	/**
	 * Gets the simulated speed
	 * @return simulated speed
	 */
	public int getSimulationSpeed(){
		return simulationSpeed;
	}

	/**
	 * Gets all states of the game
	 * @return all game states
	 */
	public GameState[] getGameState(){ return gameState; }

	/**
	 * Adds the state in parameter at last index
	 * @param gameState the new state
	 */
	public void addGameState(GameState gameState) {
		int i = this.gameState.length + 1;
		this.gameState[i] = gameState;
	}

	/**
	 * Clones the object
	 * @return Copy of the object
	 */
	@Override
	public Game clone() {
		Game game;
		try { return game = (Game)super.clone(); }
		catch (CloneNotSupportedException e) {
			System.out.println (" Cloning not allowed. " );
			return null;
		}
	}

}
