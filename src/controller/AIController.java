package controller;


import ai.EasyAI;
import ai.HardAI;
import ai.NormalAI;
import model.*;
import view.aui.ErrorAUI;
import view.aui.HintAUI;

public class AIController {

	/**
	 * An AI with difficulty Easy
	 */
	private EasyAI easyAI;

	/**
	 * An AI with difficulty Normal
	 */
	private NormalAI normalAI;

	/**
	 * An AI with difficulty Hard
	 */
	private HardAI hardAI;

	/**
	 * The Main Controller of the Control layer
	 */
	private final MainController mainController;

	/**
	 * The AUI that triggers showing of a hint
	 */
	private final HintAUI hintAUI;
	/**
	 * The Error AUI
	 */
	private final ErrorAUI errorAUI;

	/**
	 * Constructor that sets the mainController and all AUIs
	 * @param mainController The controller that knows all other controllers
	 * @param hintAUI The AUI that Triggers the Hint in the GUI
	 */
	public AIController(MainController mainController, HintAUI hintAUI,ErrorAUI errorAUI){
		this.mainController = mainController;
		this.hintAUI = hintAUI;
		this.errorAUI = errorAUI;
	}

	public void doTurn() {
		Game game = mainController.getGame();
		GameState currentGameState = game.getCurrentGameState();
		Player playerWithTurn = mainController.getGameController().getNextPlayer();
		GameState calculatedTurn = null;
		switch (playerWithTurn.getPlayerType()){
			case AI_EASY:
				calculatedTurn = easyAI.calculateTurn(currentGameState);
				break;
			case AI_MEDIUM:
				calculatedTurn = normalAI.calculateTurn(currentGameState);
				break;
			case AI_HARD:
				calculatedTurn = hardAI.calculateTurn(currentGameState);
				break;
			case HUMAN:
				break;

		}
		if(calculatedTurn == null){
			errorAUI.showError("No AI Turn Possible!");
			return;
		}
		mainController.getUndoRedoController().clearRedoList();
		game.addGameState(calculatedTurn);
		mainController.getGameController().endTurn();
	}

	/**
	 * The normal AI calculates a hint and shows it to the GUI
	 */
	public void calculateHint() {
		Game game = mainController.getGame();

		//Both Player before AI Turn
		GameState oldGameState = game.getCurrentGameState();
		Player oldPlayer1 = oldGameState.getPlayer1();
		Player oldPlayer2 = oldGameState.getPlayer2();

		//Both Player after AI Turn
		GameState newGameState =  normalAI.calculateTurn(oldGameState);
		Player newPlayer1 = newGameState.getPlayer1();
		Player newPlayer2 = newGameState.getPlayer2();

		//whose move was it
		boolean player1Move = (oldPlayer1.getBoardPosition()<=oldPlayer2.getBoardPosition());
		if(player1Move){
			decideActionDone(oldPlayer1,newPlayer1);
		}else{
			decideActionDone(oldPlayer2,newPlayer2);
		}
	}

	/**
	 * Decides what this player has done in the turn
	 * @param beforeTurn The player before the Hint-Turn
	 * @param afterTurn  The player after the Hint-Turn
	 */
	private void decideActionDone(Player beforeTurn,Player afterTurn){
		//Weather or not the Player advanced
		if(beforeTurn.getQuiltBoard().getPatches().size()==afterTurn.getQuiltBoard().getPatches().size()){
			hintAUI.showHintAdvance();
		}else{
			//Player took Patch
			Patch patchTaken = null;

			//Which Patch is new in the PatchList
			for(Patch newPatch : afterTurn.getQuiltBoard().getPatches()){
				if(!beforeTurn.getQuiltBoard().getPatches().contains(newPatch)){
					patchTaken = newPatch;
					break;
				}
			}

			//Occurs if no patch was placed
			if(patchTaken == null){
				errorAUI.showError("No Action done of AI!");
				return;
			}
			//The newly placed positions on the board
			Matrix placement = afterTurn.getQuiltBoard().getPatchBoard().without(beforeTurn.getQuiltBoard().getPatchBoard());
			hintAUI.showHintTakePatch(patchTaken,placement.toBooleanArray());
		}
	}
}
