package controller;

import model.Game;
import model.GameState;

import java.util.List;

/**
 * The Controller for Undo and Redo Operations
 * @author Yannick
 */
public class UndoRedoController {

	private MainController mainController;

	/**
	 * Constructor that sets the mainController and all AUIs
	 *
	 * @param mainController The controller that knows all other controllers
	 */
	public UndoRedoController(MainController mainController){this.mainController = mainController;}

	/**
	 * Undo.
	 */
	public void undo() {

	}

	/**
	 * Redo.
	 */
	public void redo() {
//		Game game = mainController.getGame();
//		List<GameState> gameStates = game.getGameStates();
//		if(gameStates.size()>=1&& game.getCurrentGameState()<gameStates.size())
	}

	/**
	 * Clear redo list.
	 */
	void clearRedoList(){
		Game game = mainController.getGame();
		List<GameState> gameStates = game.getGameStates();
		if(game.getCurrentGameState()+1 < gameStates.size()){
			gameStates.subList(game.getCurrentGameState()+ 1, gameStates.size()).clear();
		}
	}

}
