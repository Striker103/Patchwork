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

	private ErrorAUI errorAUI;

	/**
	 * true if errorAUI is set
	 */
	private boolean errorAUIChanged = false;


	/**
	 * Constructor that sets the mainController
	 *
	 * @param mainController The controller that knows all other controllers
	 */
	public GamePreparationController(MainController mainController){
		this.mainController = mainController;
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
		boolean parametersOk = parametersAreOk(players, speed, file);
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
		setInitialScore(ironman);
	}

	private void setInitialScore(boolean isIronman){
		mainController.getGame().getCurrentGameState().getPlayer1().createScore(isIronman);
		mainController.getGame().getCurrentGameState().getPlayer2().createScore(isIronman);
	}

	private boolean parametersAreOk(Tuple<Tuple<String,PlayerType>, Tuple<String,PlayerType>> players,int speed, File file)
	{
		if(players == null){
			errorAUI.showError("players cant be null");
			return false;
		}
		if(players.getFirst()== null || players.getSecond() == null){
			errorAUI.showError("tuples in players cant be null");
			return false;
		}
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
		if(file != null && !file.exists()) {
			errorAUI.showError("cant open csv file");
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


	public void setErrorAUI(ErrorAUI errorAUI) {
		if(this.errorAUIChanged) throw new IllegalStateException("errorAUI was already set");
		this.errorAUI = errorAUI;
		this.errorAUIChanged = true;
	}
}

