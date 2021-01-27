package controller;

import model.Game;
import model.GameState;
import model.Player;

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
		Player nextMovingPlayer = mainController.getGameController().getNextPlayer();

		//Cant undo first move with other player
		if(game.getCurrentGameStateIndex()==1){
			return;
		}

		if(game.getCurrentGameStateIndex()>0){
			game.setCurrentGameState(game.getCurrentGameStateIndex()-1);
		}else{
			//only 1 GameState
			return;
		}
		game.setHighScoreReachable(false);
		//Find last gamestate with same moving player
		boolean moveWasNotFirstMove = false;
		while(game.getCurrentGameStateIndex()>0){
			moveWasNotFirstMove = true;
			if(mainController.getGameController().getNextPlayer() == nextMovingPlayer){
				break;
			}else{
				game.setCurrentGameState(game.getCurrentGameStateIndex()-1);
			}
		}
	}

	/**
	 * Sets the current GameState to successor
	 */
	public void redo() {
		Game game = mainController.getGame();
		List<GameState> gameStates = game.getGameStates();
		Player movingPlayer = mainController.getGameController().getNextPlayer();
		if(!game.currentGameStateLast()){
			game.setCurrentGameState(game.getCurrentGameStateIndex()+1);
		}
		//Find next gamestate with same moving player
		while(!game.currentGameStateLast()&&mainController.getGameController().getNextPlayer()!=movingPlayer){
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
