package controller;


import ai.EasyAI;
import ai.HardAI;
import ai.NormalAI;
import model.*;
import view.aui.ErrorAUI;
import view.aui.HintAUI;
import view.aui.LogAUI;

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
	private HintAUI hintAUI;
	/**
	 * The Error AUI
	 */
	private ErrorAUI errorAUI;
	/**
	 * The Error AUI
	 */
	private LogAUI logAUI;

	/**
	 * true if hintAUI is set
	 */
	private boolean hintAUIChanged = false;

	/**
	 * true if errorAUI is set
	 */
	private boolean errorAUIChanged = false;

	/**
	 * true if logAUI is set
	 */
	private boolean logAUIChanged = false;

	/**
	 * Constructor that sets the mainController
	 *
	 * @param mainController The controller that knows all other controllers
	 */
	public AIController(MainController mainController){
		this.mainController = mainController;
		easyAI = new EasyAI();
		normalAI = new NormalAI();
		hardAI = new HardAI();

	}

	public void doTurn() {
		Game game = mainController.getGame();
		GameState currentGameState = game.getCurrentGameState();
		Player playerWithTurn = mainController.getGameController().getNextPlayer();
		GameState calculatedTurn = null;
		switch (playerWithTurn.getPlayerType()){
			case AI_EASY:
				calculatedTurn = easyAI.calculateTurn(currentGameState,playerWithTurn);
				break;
			case AI_MEDIUM:
				calculatedTurn = normalAI.calculateTurn(currentGameState,playerWithTurn);
				break;
			case AI_HARD:
				calculatedTurn = hardAI.calculateTurn(currentGameState,playerWithTurn);
				break;
			case HUMAN:
				break;

		}
		if(calculatedTurn == null){
			errorAUI.showError("No AI Turn Possible!");
			return;
		}
		logAUI.updateLog(calculatedTurn.getLogEntry());
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
		GameState newGameState =  normalAI.calculateTurn(oldGameState,mainController.getGameController().getNextPlayer());
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

	/**
	 * set the errorAUI
	 * @param errorAUI the errorAUI
	 */
	public void setErrorAUI(ErrorAUI errorAUI) {
		if(this.errorAUIChanged) throw new IllegalStateException("errorAUI was already set");
		this.errorAUI = errorAUI;
		this.errorAUIChanged = true;
	}

	/**
	 * set the hintAUI
	 * @param hintAUI the hintAUI
	 */
	public void setHintAUI(HintAUI hintAUI) {
		if(this.hintAUIChanged) throw new IllegalStateException("hintAUI was already set");
		this.hintAUI = hintAUI;
		this.hintAUIChanged = true;
	}

	/**
	 * set the logAUI
	 * @param logAUI the logAUI
	 */
	public void setLogAUI(LogAUI logAUI) {
		if(this.hintAUIChanged) throw new IllegalStateException("logAUI was already set");
		this.logAUI = logAUI;
		this.logAUIChanged = true;
	}
}
