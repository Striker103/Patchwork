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
	public MainController()
	{
		gamePreparationController = new GamePreparationController(this);
		gameController = new GameController(this);
		aIController = new AIController(this);
		playerController = new PlayerController(this);
		undoRedoController = new UndoRedoController(this);
		iOController = new IOController(this);
		highScoreController = new HighScoreController(this);
	}

	/**
	 * sets all HighScoreAUIs
	 * @param highScoreAUI the highScoreAUI
	 */
	public void setHighScoreAUI(HighScoreAUI highScoreAUI){
		highScoreController.setHighScoreAUI(highScoreAUI);
	}

	/**
	 * sets all Error AUIs
	 * @param errorAUI the errorAUI
	 */
	public void setErrorAUI(ErrorAUI errorAUI){
		gamePreparationController.setErrorAUI(errorAUI);
		gameController.setErrorAUI(errorAUI);
		aIController.setErrorAUI(errorAUI);
		playerController.setErrorAUI(errorAUI);
		iOController.setErrorAUI(errorAUI);
		highScoreController.setErrorAUI(errorAUI);
	}

	/**
	 * sets all hint AUIs
	 * @param hintAUI the hintAUI
	 */
	public void setHintAUI(HintAUI hintAUI){
		aIController.setHintAUI(hintAUI);
	}

	/**
	 * sets all logAUIs
	 * @param logAUI the logAUI
	 */
	public void setLogAUI(LogAUI logAUI){
		gameController.setLogAUI(logAUI);
		playerController.setLogAUI(logAUI);
		aIController.setLogAUI(logAUI);
	}

	/**
	 * sets all turnAUIs
	 * @param turnAUI the turnAUI
	 */
	public void setTurnAUI(TurnAUI turnAUI){
		gameController.setTurnAUI(turnAUI);
		playerController.setTurnAUI(turnAUI);
		undoRedoController.setTurnAUI(turnAUI);
	}

	/**
	 * sets all loadGameAUIs
	 * @param loadGameAUI the loadGameAUI
	 */
	public void setLoadGameAUI(LoadGameAUI loadGameAUI){
		iOController.setLoadGameAUI(loadGameAUI);
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
	public void setGame(Game game) {
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
