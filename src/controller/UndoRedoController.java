package controller;

import model.Game;
import model.GameState;
import model.Player;
import view.aui.TurnAUI;

import java.util.List;

/**
 * The Controller for Undo and Redo Operations
 * @author Yannick
 */
public class UndoRedoController {

	private final MainController mainController;

	private TurnAUI turnAUI;

	/**
	 * true if logAUI is changed
	 */
	private boolean turnAUIChanged = false;

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
		while(game.getCurrentGameStateIndex()>0){
			if(mainController.getGameController().getNextPlayer().getName().equals(nextMovingPlayer.getName())){
				break;
			}else{
				game.setCurrentGameState(game.getCurrentGameStateIndex()-1);
			}
		}
		turnAUI.updatePatches();
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
		turnAUI.updatePatches();
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

	/**
	 * set the turnAUi
	 *
	 * @param turnAUI the turnAUI
	 */
	public void setTurnAUI(TurnAUI turnAUI) {
		if(this.turnAUIChanged) throw new IllegalStateException("turnAUI was already set");
		this.turnAUI = turnAUI;
		this.turnAUIChanged = true;
	}


}
