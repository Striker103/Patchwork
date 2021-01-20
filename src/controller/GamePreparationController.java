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
		boolean parametersOk = parametersAreOk(players, speed);
		if(!parametersOk)
			return;
		List<Patch> patches;
		patches = getPatches(file);
		Game game;
		if(speed == 0)
		{
			game = new Game(ironman);
		}else{
			game = new Game(ironman, speed);
		}

		GameState gameState = new GameState(players, patches);
		game.addGameState(gameState);
		mainController.setGame(game);
		GameController gameController = mainController.getGameController();
		gameController.endTurn();
	}

	private boolean parametersAreOk(Tuple<Tuple<String,PlayerType>, Tuple<String,PlayerType>> players,int speed)
	{
		if(players.getFirst().getSecond() == null || players.getSecond().getSecond() == null){
			errorAUI.showError("player types cant be null");
			return false;
		}
		if(players.getFirst().getFirst() == null || players.getSecond().getFirst() == null){
			errorAUI.showError("player names cant be null");
			return false;
		}
		if(speed < 0){
			errorAUI.showError("speed must be positive or 0");
			return false;
		}
		if(players.getFirst().getFirst().isBlank() || players.getSecond().getFirst().isBlank()) {
			errorAUI.showError("at least one player name is missing");
			return false;
		}
		if(players.getFirst().getFirst().equals(players.getSecond().getFirst())) {
			errorAUI.showError("player names should not be equal");
			return false;
		}
		return true;
	}

	public void startGame(Tuple<Tuple<String,PlayerType>, Tuple<String,PlayerType>> players, File file, boolean ironman) {
		startGame(players, file, 0, ironman);
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

