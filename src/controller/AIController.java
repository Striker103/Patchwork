package controller;


import ai.EasyAI;
import ai.HardAI;
import ai.NormalAI;
import model.*;
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
	private MainController mainController;

	/**
	 * The AUI that triggers showing of a hint
	 */
	private HintAUI hintAUI;

	/**
	 * Constructor that sets the mainController and all AUIs
	 * @param mainController The controller that knows all other controllers
	 * @param hintAUI The AUI that Triggers the Hint in the GUI
	 */
	public AIController(MainController mainController, HintAUI hintAUI){
		this.mainController = mainController;
		this.hintAUI = hintAUI;
	}

	public GameState doTurn() {
		return null;
	}

	public GameState calculateHint() {
		Game game = mainController.getGame();
		GameState oldGameState = game.getCurrentGameState();
		Player oldPlayer1 = oldGameState.getPlayer1();
		Player oldPlayer2 = oldGameState.getPlayer2();

		GameState newGameState =  normalAI.calculateTurn(oldGameState);
		Player newPlayer1 = newGameState.getPlayer1();
		Player newPlayer2 = newGameState.getPlayer2();

		//whose move was it
		boolean player1Move = (oldPlayer1.getBoardPosition()<=oldPlayer2.getBoardPosition());

		if(player1Move){
			if(oldPlayer1.getQuiltBoard().getPatches().size()==newPlayer1.getQuiltBoard().getPatches().size()){
				hintAUI.showHintAdvance();
			}else{
				Patch patchTaken;
				for(Patch newPatch : newPlayer1.getQuiltBoard().getPatches()){
					if(!oldPlayer1.getQuiltBoard().getPatches().contains(newPatch)){
						patchTaken = newPatch;
						break;
					}
				}
				//Matrix placement =
			}
		}

	return null;


	}
}
