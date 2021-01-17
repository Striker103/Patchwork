package controller;


import ai.EasyAI;
import ai.HardAI;
import ai.NormalAI;
import model.GameState;
import view.aui.HintAUI;

public class AIController {

	private EasyAI[] easyAI;

	private NormalAI[] normalAI;

	private HardAI[] hardAI;

	private MainController mainController;

	private HintAUI hintAUI;

	public GameState doTurn() {
		return null;
	}

	public GameState calculateHint() {
		return null;
	}

	/**
	 * Constructor that sets the mainController and all AUIs
	 * @param mainController The controller that knows all other controllers
	 * @param hintAUI the hintAUI
	 */
	public AIController(MainController mainController, HintAUI hintAUI){
		this.mainController = mainController;
		this.hintAUI = hintAUI;
	}

}
