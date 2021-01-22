package controller;

import model.Game;
import model.GameState;

import java.util.List;

/**
 * The Controller for Undo and Redo Operations
 * @author Yannick
 */
public class UndoRedoController {

	private final MainController mainController;

	/**
	 * Constructor that sets the mainController
	 *
	 * @param mainController The controller that knows all other controllers
	 */
	public UndoRedoController(MainController mainController){
		this.mainController = mainController;
	}

	/**
	 * Sets the current GameState to its predecessor
	 */
	public void undo() {
		Game game = mainController.getGame();
		List<GameState> gameStates = game.getGameStates();
		if(game.getCurrentGameStateIndex()>0){
			game.setCurrentGameState(game.getCurrentGameStateIndex()-1);
		}
	}

	/**
	 * Sets the current GameState to successor
	 */
	public void redo() {
		Game game = mainController.getGame();
		List<GameState> gameStates = game.getGameStates();
		if(!game.currentGameStateLast()){
			game.setCurrentGameState(game.getCurrentGameStateIndex()+1);
		}
	}

	/**
	 * Clears the List of possible redoes
	 */
	void clearRedoList(){
		Game game = mainController.getGame();
		List<GameState> gameStates = game.getGameStates();
		if(!game.currentGameStateLast()){
			gameStates.subList(game.getCurrentGameStateIndex()+ 1, gameStates.size()).clear();
		}
	}


}
