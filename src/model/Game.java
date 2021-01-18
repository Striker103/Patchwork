package model;

import java.util.ArrayList;
import java.util.List;

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
	private List<GameState> gameStates;

	/**
	 * Constructor for a new Game Object with given speed of simulation
	 * @param pIronman the Game mode
	 * @param pSimulationSpeed the speed of the simulation
	 */
	public Game(boolean pIronman, int pSimulationSpeed){
		this( pIronman );
		if (pSimulationSpeed > 0) {
			simulationSpeed = pSimulationSpeed;
		} else {
			throw new IllegalArgumentException("The simulationSpeed must to be positive value");
		}
	}

	/**
	 * Constructor for a new Game Object
	 * @param pIronman the Game mode
	 */
	public Game(boolean pIronman) {
		this.currentGameState = 0;
		this.highscoreReachable = false;
		this.simulationSpeed = 0;
		this.gameStates = new ArrayList<GameState>();
		ironman = pIronman;
	}

	/**
	 * Gets the index of the current state of the game
	 * @return the currently state
	 */
	public int getCurrentGameStateIndex(){
		return currentGameState;
	}

	/**
	 * Gets the currently state of the game
	 * @return the currently state
	 */
	public GameState getCurrentGameState(){
		return gameStates.get(currentGameState);
	}

	/**
	 * Sets the current GameState
	 * @param currentGameState the new current GameState
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void setCurrentGameState(int currentGameState) {
		try{
			gameStates.get(currentGameState);
		}catch(Exception e){
			throw new IllegalStateException("No Element at that index!");
		}
		this.currentGameState = currentGameState;
	}

	/**
	 * Gets the reachabled highscore
	 * @return the reachabled highscore
	 */
	public boolean isHighscoreReachable(){
		return highscoreReachable;
	}

	/**
	 * Gets the game mode
	 * @return the ironman mode
	 */
	public boolean isIronman(){
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
	public List<GameState> getGameStates(){ return gameStates; }

	/**
	 * Adds the state in parameter at last index
	 * @param gameState the new state
	 */
	public void addGameState(GameState gameState) {
		gameStates.add(gameState);
	}

	/**
	 * Clones the object
	 * @return Copy of the object
	 */
	public Game copy() {
		return new Game(ironman);
	}
	/**
	 * Checks if the current GameState has a Successor
	 * @return Weather or not the current GameState is the last
	 *
	*/
	public boolean currentGameStateLast(){
		return currentGameState == gameStates.size()-1;
	}



}
