package controller;


import model.*;
import view.aui.*;

import java.io.File;
import java.util.List;

/**
 * sets up new Games
 *
 * @author Dennis
 */
public class GamePreparationController {

	private final MainController mainController;

	private final ErrorAUI errorAUI;


	/**
	 * Constructor that sets the mainController and all AUIs
	 *
	 * @param mainController The controller that knows all other controllers
	 * @param errorAUI the errorAUI
	 */
	public GamePreparationController(MainController mainController, ErrorAUI errorAUI) {
		this.mainController = mainController;
		this.errorAUI = errorAUI;
	}

	/**
	 * sets up a new game
	 *
	 * @param players two tuples each with name and playerType in it
	 * @param file the csv file of the patches (can be null)
	 * @param speed the simulation speed of two AI who fight against each other
	 * @param ironman if true undo, redo and tips are not possible
	 */
	public void startGame(Tuple<Tuple<String,PlayerType>, Tuple<String,PlayerType>> players, File file, int speed, boolean ironman) {
		CheckUtil.assertNonNull(players.getFirst().getSecond(), players.getSecond().getSecond(), ironman, speed);
		CheckUtil.assertNonNull(players.getFirst().getFirst(),players.getSecond().getFirst());
		CheckUtil.assertNonNegative(speed);
		if(players.getFirst().getFirst().isBlank() || players.getSecond().getFirst().isBlank()) {
			errorAUI.showError("at least one player name is missing");
			return;
		}
		if(players.getFirst().getFirst().equals(players.getSecond().getFirst())) {
			errorAUI.showError("player names should not be equal");
			return;
		}
		List<Patch> patches;
		patches = getPatches(file);

		Game game = new Game(ironman, speed);

		GameState gameState = new GameState(players, patches);
		game.addGameState(gameState);
		mainController.setGame(game);
		GameController gameController = mainController.getGameController();
		gameController.endTurn();
	}

	private List<Patch> getPatches(File file)
	{
		List<Patch> patches;
		IOController ioController = mainController.getIOController();
		if(file == null)
		{
			patches = ioController.importCSV();
		} else {
			patches = ioController.importCSV(file);
		}
		return patches;
	}



}

