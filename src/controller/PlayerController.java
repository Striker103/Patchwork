package controller;


import model.Patch;
import view.aui.ErrorAUI;

public class PlayerController {

	private MainController mainController;

	private ErrorAUI errorAUI;

	/**
	 * Constructor that sets the mainController and all AUIs
	 * @param mainController The controller that knows all other controllers
	 * @param errorAUI the errorAUI
	 */
	public PlayerController(MainController mainController, ErrorAUI errorAUI){
		this.mainController = mainController;
		this.errorAUI = errorAUI;
	}

	void getIncome() {

	}

	public void moveTimeToken(int steps) {

	}

	public void payPatch(Patch patch) {

	}



}
