package controller;


import ai.EasyAI;
import ai.HardAI;
import ai.NormalAI;
import model.GameState;
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
		mainController.getGame();
		return normalAI.calculateTurn(null);
	}
}
