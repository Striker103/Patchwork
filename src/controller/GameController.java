package controller;


import model.*;
import view.aui.ErrorAUI;
import view.aui.LogAUI;
import view.aui.TurnAUI;

public class GameController {

	private final MainController mainController;

	private final ErrorAUI errorAUI;

	private final LogAUI logAUI;

	private final TurnAUI turnAUI;

	/**
	 * Constructor that sets the mainController and all AUIs
	 * @param mainController The controller that knows all other controllers
	 * @param errorAUI the errorAUI
	 * @param logAUI the logAUI
	 * @param turnAUI the turnAUI
	 */
	public GameController(MainController mainController, ErrorAUI errorAUI, LogAUI logAUI, TurnAUI turnAUI){
		this.mainController = mainController;
		this.errorAUI = errorAUI;
		this.logAUI = logAUI;
		this.turnAUI = turnAUI;
	}

	public void advance() {
		//TODO

		cloneGameState();
		endTurn();
	}

	public void takePatch(Patch patch, boolean[][] placing) {

		//TODO
		cloneGameState();
		endTurn();
	}

	/**
	 * Triggers the next Human or AI turn
	 */
	void endTurn() {
		Player nextMovingPlayer = getNextPlayer();
		AIController aiController = mainController.getAIController();
		if(nextMovingPlayer.getPlayerType()== PlayerType.HUMAN){
			turnAUI.triggerPlayerTurn();
		}else {
			aiController.doTurn();
		}
	}

	/**
	 * Makes a copy of the current GameState and appends it
	 */
	void cloneGameState(){
		mainController.getGame().addGameState(mainController.getGame().getCurrentGameState().copy());
	}

	/**
	 * Returns the next moving player
	 * @return the player furthest behind on the board
	 */
	public Player getNextPlayer(){
		return getNextPlayer(mainController.getGame().getCurrentGameStateIndex());
	}

	/**
	 * Looks which player is behind. If both have an equal Position, look who came last to that field
	 * @param gameStateIndex The Gamestate to calculate Player position
	 * @return next moving Player
	 */
	private Player getNextPlayer(int gameStateIndex) {
		GameState gameState = mainController.getGame().getGameStates().get(gameStateIndex);
		Player player1 = gameState.getPlayer1();
		Player player2 = gameState.getPlayer2();


		if(player1.getBoardPosition()<player2.getBoardPosition()||player1.getBoardPosition()==0){
			return player1;
		}else if(player1.getBoardPosition()>player2.getBoardPosition() ){
			return player2;
		}else{
			return getNextPlayer(gameStateIndex-1);
		}
	}

}
