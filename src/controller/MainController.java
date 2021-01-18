package controller;


import model.Game;
import view.aui.*;

/**
 * The Controller which creates and holds all other controllers
 *
 * @author Dennis
 */

public class MainController {

	/**
	 * GamePreparationController
	 */
	private final GamePreparationController gamePreparationController;

	/**
	 * GameController
	 */
	private final GameController gameController;

	/**
	 * AIController
	 */
	private final AIController aIController;

	/**
	 * PlayerController
	 */
	private final PlayerController playerController;

	/**
	 * UndoRedoController
	 */
	private final UndoRedoController undoRedoController;

	/**
	 * iOController
	 */
	private final IOController iOController;

	/**
	 * highScoreController
	 */
	private final HighScoreController highScoreController;

	/**
	 * Game
	 */
	private Game game;

	/**
	 * generates all controllers and their AUIs
	 */
	public MainController(HighscoreAUI highscoreAUI, ErrorAUI errorAUI, HintAUI hintAUI, LogAUI logAUI, TurnAUI turnAUI, LoadGameAUI loadGameAUI)
	{
		gamePreparationController = new GamePreparationController(this, errorAUI);
		gameController = new GameController(this, errorAUI, logAUI, turnAUI);
		aIController = new AIController(this, hintAUI);
		playerController = new PlayerController(this, errorAUI);
		undoRedoController = new UndoRedoController(this);
		iOController = new IOController(this, errorAUI, loadGameAUI);
		highScoreController = new HighScoreController(this, errorAUI, highscoreAUI);
	}

	/**
	 * checks if the attribute game is set
	 * @return true if a game is set
	 */
	public boolean hasGame(){
		return game != null;
	}

	/**
	 * sets the game
	 * @param game the new game
	 */
	void setGame(Game game) {
		this.game = game;
	}

	/**
	 * returns the current game
	 * @return the game
	 */
	public Game getGame(){
		return game;
	}

	/**
	 * returns a GamePreparationController
	 * @return GamePreparationController
	 */
	public GamePreparationController getGamePreparationController() {
		return gamePreparationController;
	}

	/**
	 * returns a GameController
	 * @return GameController
	 */
	public GameController getGameController() {
		return gameController;
	}

	/**
	 * returns an getAIController
	 * @return aIController
	 */
	public AIController getAIController() {
		return aIController;
	}

	/**
	 * returns a PlayerController
	 * @return playerController
	 */
	public PlayerController getPlayerController() {
		return playerController;
	}

	/**
	 * returns a UndoRedoController
	 * @return undoRedoController
	 */
	public UndoRedoController getUndoRedoController() {
		return undoRedoController;
	}

	/**
	 * returns an IOController
	 * @return iOController
	 */
	public IOController getIOController() {
		return iOController;
	}

	/**
	 * returns an HighScoreController
	 * @return highScoreController
	 */
	public HighScoreController getHighScoreController() {
		return highScoreController;
	}
}
