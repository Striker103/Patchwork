package controller;


import model.Patch;
import model.Player;
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

	}

	public void takePatch(Patch patch, boolean[][] placing) {

	}

	void endTurn() {

	}

	/**
	 * Returns the next player
	 * @return next player
	 */
	public Player getCurrentPlayer()
	{
		return null;
	}

}
