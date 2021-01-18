package controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;
import view.aui.ErrorAUI;
import view.aui.HighscoreAUI;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class HighScoreController {

	private HighscoreAUI highscoreAUI;

	private MainController mainController;

	private ErrorAUI errorAUI;





	/**
	 * Constructor that sets the mainController and all AUIs
	 * @param mainController The controller that knows all other controllers
	 * @param errorAUI the errorAUI
	 * @param highscoreAUI the highScoreAUI
	 */
	public HighScoreController(MainController mainController, ErrorAUI errorAUI, HighscoreAUI highscoreAUI){
		this.mainController = mainController;
		this.errorAUI = errorAUI;
		this.highscoreAUI = highscoreAUI;
	}

	/**
	 * Saves the scores of the current game state for all human players to the specified file
	 * @param file file
	 */
	public void saveScores(File file) {

		if(!mainController.hasGame()) {
			errorAUI.showError("No game existing.");
			return;
		}

		Player player1 = mainController.getGame().getGameStates().get(mainController.getGame().getCurrentGameState()).getPlayer1();
		Player player2 = mainController.getGame().getGameStates().get(mainController.getGame().getCurrentGameState()).getPlayer2();

		try {
			CheckUtil.assertNonNull(player1, player2);
		} catch (IllegalArgumentException e) {
			errorAUI.showError("Player null");
			return;
		}

		if (player1.getPlayerType() == PlayerType.HUMAN)
			savePlayerScore(player1, file);

		if (player2.getPlayerType() == PlayerType.HUMAN)
			savePlayerScore(player2, file);

	}

	/**
	 * Clears highscore file
	 * @param file file
	 */
	public void clearHighscores(File file) {
		try {
			new PrintWriter(file).close();
		} catch (FileNotFoundException e) {
			errorAUI.showError("IOError occurred.");
		}
	}

	/**
	 * Shows the highscores specified in the file
	 * @param file file
	 */
	public void showHighScores(File file){
		highscoreAUI.showHighscores(readHighscores(file));
	}


	/**
	 * Recalculates the player's score and updates the score
	 * @param player player
	 */
	public void updateScore(Player player) {

		//Calculate money
		int scoreValue = player.getMoney();

		Matrix board = player.getQuiltBoard().getPatchBoard();

		//Calculate empty spaces
		scoreValue -= board.count(0);

		//Calculate special tile
		//TODO

		player.getScore().setValue(scoreValue);

	}

	private void savePlayerScore(Player player, File file){

		Score score = player.getScore();

		LinkedList<Score> scores = readHighscores(file);
		scores.add(score);

		scores.sort(Comparator.comparingInt(Score::getValue));

		clearHighscores(file);
		writeHighscores(scores, file);
	}

	private void writeHighscores(LinkedList<Score> scores, File file){

		CheckUtil.assertNonNull(scores);

		Gson gson = new Gson();
		String json = gson.toJson(scores);

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(json);

			writer.close();
		} catch (IOException exception) {
			errorAUI.showError("IOException occurred.");
		}
	}

	private LinkedList<Score> readHighscores(File file) {

		try {
			String json = Files.readString(file.toPath());

			Gson gson = new Gson();
			return gson.fromJson(json, new TypeToken<LinkedList<Score>>(){}.getType());

		} catch (IOException exception) {
			errorAUI.showError("IOError occurred.");
			return null;
		}

	}

//	public void setErrorAUI(ErrorAUI errorAUI){
//		if (this.errorAUI == null)
//			this.errorAUI = errorAUI;
//		else
//			throw new IllegalStateException("ErrorAUI is already set");
//	}
//
//	public void setHighscoreAUI(HighscoreAUI highscoreAUI){
//		if (this.highscoreAUI == null)
//			this.highscoreAUI = highscoreAUI;
//		else
//			throw new IllegalStateException("HighscoreAUI is already set");
//	}

}
