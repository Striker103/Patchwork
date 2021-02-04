package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
	private boolean highScoreReachable;

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
		this.gameStates = new ArrayList<GameState>();
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
		this.currentGameState = -1;
		this.highScoreReachable = true;
		this.simulationSpeed = 0;
		this.gameStates = new ArrayList<GameState>();
		ironman = pIronman;
	}

	/**
	 * returns the list with the game states
	 * @return a list with all gameStates
	 */
	public List<GameState> getGameStatesList(){
		return gameStates;
	}

	/**
	 * Copy Constructor
	 */
	private Game(int currentGameState, boolean highScoreReachable, boolean ironman, int simulationSpeed, List<GameState> gameStates) {
		this.currentGameState = currentGameState;
		this.highScoreReachable = highScoreReachable;
		this.ironman = ironman;
		this.simulationSpeed = simulationSpeed;
		List<GameState> copiedGameStates = new ArrayList<>();
		for(GameState gameState : gameStates){
			copiedGameStates.add(gameState.copy());
		}
		this.gameStates = copiedGameStates;
	}

	/**
	 * Gets the index of the current state of the game
	 * @return the currently state
	 */
	public int getCurrentGameStateIndex(){
		return currentGameState;
	}

	/**
	 * Gets the currently state of the game.
	 * @return the currently state
	 */
	public GameState getCurrentGameState(){
		try{
			return gameStates.get(currentGameState);
		}catch(Exception e){
			throw new IllegalStateException("no game state");
		}
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
	public boolean isHighScoreReachable(){
		return highScoreReachable;
	}

	/**
	 * Sets the new highScore reachable
	 * @param highScoreReachable the new highScore reachable
	 */
	public void setHighScoreReachable(boolean highScoreReachable){
		this.highScoreReachable = highScoreReachable;
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
		if(!currentGameStateLast()){
			throw new IllegalStateException("the game state is not the last element");
		}
		gameStates.add(gameState);
		currentGameState++;
	}

	/**
	 * Clones the object
	 * @return Copy of the object
	 */
	public Game copy() {
		return new Game(currentGameState, highScoreReachable,ironman,simulationSpeed,getGameStates());
	}

	/**
	 * Checks if the current GameState has a Successor
	 * @return Weather or not the current GameState is the last
	 *
	*/
	public boolean currentGameStateLast(){
		return currentGameState == gameStates.size()-1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Game game = (Game) obj;
		return currentGameState == game.currentGameState &&
				highScoreReachable == game.highScoreReachable &&
				ironman == game.ironman &&
				simulationSpeed == game.simulationSpeed &&
				gameStates.equals(game.gameStates);
	}

	@Override
	public int hashCode() {
		return Objects.hash(currentGameState, highScoreReachable, ironman, simulationSpeed, gameStates);
	}
}
