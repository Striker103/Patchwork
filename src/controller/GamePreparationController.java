package controller;


import model.Patch;
import model.Player;
import model.Tuple;
import view.aui.ErrorAUI;

import java.io.File;
import java.util.List;

public class GamePreparationController {

	private final MainController mainController;

	private final ErrorAUI errorAUI;

	/**
	 * Constructor that sets the mainController and all AUIs
	 * @param mainController The controller that knows all other controllers
	 * @param errorAUI the errorAUI
	 */
	public GamePreparationController(MainController mainController, ErrorAUI errorAUI) {
		this.mainController = mainController;
		this.errorAUI = errorAUI;
	}

	private List<Patch> sortPatches(List<Patch> patches) {
		return null;
	}

	public void startGame(Tuple<Player,Player> players, File file, int simulationSpeed, boolean ironman) {

	}

	private void buildInitialGameState(Tuple<Player,Player> players, List<Patch> patches) {

	}

}
