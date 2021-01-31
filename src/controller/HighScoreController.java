package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;
import view.aui.ErrorAUI;
import view.aui.HighScoreAUI;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Controller for high score management
 *
 * @author Alexandra Latys
 */
public class HighScoreController {

	private HighScoreAUI highscoreAUI;

	private final MainController mainController;

	private ErrorAUI errorAUI;

	/**
	 * Constructor that sets the mainController
	 *
	 * @param mainController The controller that knows all other controllers
	 */
	public HighScoreController(MainController mainController){
		this.mainController = mainController;
	}

	/**
	 * Saves the scores of the current game state for all human players to the specified file
	 * @param file file
	 */
	public void saveScores(File file) {

		if (file == null){
			errorAUI.showError("Invalid file.");
			return;
		}

		String[] split = file.toPath().toString().split("\\.");
		String ending = split[split.length - 1];

		if (!ending.equals("json")){
			errorAUI.showError("Wrong file extension");
			return;
		}

		if (!file.exists()){
			try {
				if (!file.createNewFile()){
					errorAUI.showError("File already exists.");
					return;
				}
			} catch (IOException e) {
				errorAUI.showError("IOException occurred.");
				return;
			}
		}

		if (file.isDirectory() || !file.canWrite()){
			errorAUI.showError("Invalid file.");
			return;
		}



		if(!mainController.hasGame()) {
			errorAUI.showError("No game existing.");
			return;
		}

		if (!mainController.getGame().isHighScoreReachable()){
			errorAUI.showError("High score is not reachable.");
			return;
		}

		Player player1 = mainController.getGame().getCurrentGameState().getPlayer1();
		Player player2 = mainController.getGame().getCurrentGameState().getPlayer2();

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
	 * Clears high score file
	 * @param file file
	 */
	public void clearHighScores(File file) {
		try {
			new PrintWriter(file).close();
		} catch (FileNotFoundException e) {
			errorAUI.showError("IOError occurred.");
		}
	}

	/**
	 * Shows the high scores specified in the file
	 * @param file file
	 */
	public void showHighScores(File file){

		if (file == null){
			errorAUI.showError("Invalid file.");
			return;
		}

		String[] split = file.toPath().toString().split("\\.");
		String ending = split[split.length - 1];

		if (!ending.equals("json")){
			errorAUI.showError("Wrong file extension");
			return;
		}

		if (!file.exists()){
			try {
				if (!file.createNewFile()){
					errorAUI.showError("File already exists.");
					return;
				}
			} catch (IOException e) {
				errorAUI.showError("IOException occurred.");
				return;
			}
		}

		if (file.isDirectory() || !file.canWrite()){
			errorAUI.showError("Invalid file.");
			return;
		}

		highscoreAUI.showHighscores(readHighScores(file));
	}


	/**
	 * Recalculates the player's score and updates the score
	 * @param player player
	 */
	public void updateScore(Player player) {

		if (player == null)
			return;

		//Calculate money
		int scoreValue = player.getMoney();

		Matrix board = player.getQuiltBoard().getPatchBoard();

		//Calculate empty spaces
//		scoreValue -= 2 * board.count(0);

		int count = 0;
		int[][] matrixArray = board.getIntMatrix();

		for (int i = 0; i < 9 ; i++){
			for (int j = 0; j < 9 ; j++){
				if (matrixArray[i][j] == 0)
					count += 1;
			}
		}

		scoreValue -= 2 * count;

		//Calculate special tile
		if (player.getHasSpecialTile())
			scoreValue += 7;

		player.getScore().setValue(scoreValue);

	}

	private void savePlayerScore(Player player, File file){

		Score score = player.getScore();

		LinkedList<Score> scores = readHighScores(file);


		if (scores != null) {
			scores.add(score);

			scores.sort(Comparator.comparingInt(Score::getValue));

			clearHighScores(file);
			writeHighScores(scores, file);
		}
	}

	private void writeHighScores(LinkedList<Score> scores, File file){

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

	private LinkedList<Score> readHighScores(File file) {

		try {
			if (!file.exists()){
				if (!file.createNewFile()) {
					errorAUI.showError("File already exists.");
					return null;
				}


			}

		} catch (IOException e) {
			errorAUI.showError("IOException occurred.");
			return null;
		}

		try {
			String json = Files.readString(file.toPath());

			Gson gson = new Gson();

			LinkedList<Score> list = gson.fromJson(json, new TypeToken<LinkedList<Score>>(){}.getType());

			if (list != null){
				return list;
			} else {
				return new LinkedList<>();
			}

		} catch (IOException exception) {
			errorAUI.showError("IOError occurred.");
			return null;
		}

	}



	/**
	 * set the errorAUI
	 * @param errorAUI the errorAUI
	 */
	public void setErrorAUI(ErrorAUI errorAUI){
		if (this.errorAUI == null)
			this.errorAUI = errorAUI;
		else
			throw new IllegalStateException("ErrorAUI is already set");
	}


	/**
	 * set the highScoreAUI
	 * @param highscoreAUI the highscoreAUI
	 */
	public void setHighScoreAUI(HighScoreAUI highscoreAUI){
		if (this.highscoreAUI == null)
			this.highscoreAUI = highscoreAUI;
		else
			throw new IllegalStateException("HighScoreAUI is already set");
	}

}
