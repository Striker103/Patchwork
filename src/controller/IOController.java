package controller;


import model.Patch;
import view.aui.ErrorAUI;

import java.io.File;
import java.util.List;

public class IOController {

	private MainController mainController;

	private ErrorAUI errorAUI;

	/**
	 * Constructor that sets the mainController and all AUIs
	 * @param mainController The controller that knows all other controllers
	 * @param errorAUI the errorAUI
	 */
	public IOController(MainController mainController, ErrorAUI errorAUI){
		this.mainController = mainController;
		this.errorAUI = errorAUI;
	}

	public void saveGame() {

	}

	public void loadGame() {

	}

	public void exportGameResult() {

	}

	public List<Patch> importCSV(File file) {
		return null;
	}

	public List<Patch> importCSV() {
		return null;
	}

}
