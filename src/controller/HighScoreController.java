package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;
import view.aui.ErrorAUI;
import view.aui.HighScoreAUI;

import java.io.*;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.LinkedList;

public class HighScoreController {

	private HighScoreAUI highscoreAUI;

	private MainController mainController;

	private ErrorAUI errorAUI;

	/**
	 * true if errorAUI is set
	 */
	private boolean errorAUIChanged = false;

	/**
	 * true if errorAUI is set
	 */
	private boolean highscoreAUIChanged = false;

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

		if(!mainController.hasGame()) {
			errorAUI.showError("No game existing.");
			return;
		}

		//TODO es gibt jetzt auch getCurrentGameState :D
		Player player1 = mainController.getGame().getGameStates().get(mainController.getGame().getCurrentGameStateIndex()).getPlayer1();
		Player player2 = mainController.getGame().getGameStates().get(mainController.getGame().getCurrentGameStateIndex()).getPlayer2();

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
		highscoreAUI.showHighscores(readHighScores(file));
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
		scoreValue -= 2 * board.count(0);

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
			String json = Files.readString(file.toPath());

			Gson gson = new Gson();
			return gson.fromJson(json, new TypeToken<LinkedList<Score>>(){}.getType());

		} catch (IOException exception) {
			errorAUI.showError("IOError occurred.");
			return null;
		}

	}

	/**
	 * set the highScoreAUI
	 * @param highscoreAUI the highscoreAUI
	 */
    public void setHighScoreAUI(HighScoreAUI highscoreAUI) {
		if(this.highscoreAUIChanged) throw new IllegalStateException("highscoreAUI was already set");
		this.highscoreAUI = highscoreAUI;
		this.highscoreAUIChanged = true;
    }

	/**
	 * set the errorAUI
	 * @param errorAUI the errorAUI
	 */
	public void setErrorAUI(ErrorAUI errorAUI) {
		this.errorAUI = errorAUI;
		if(this.errorAUIChanged) throw new IllegalStateException("errorAUI was already set");
		this.errorAUI = errorAUI;
		this.errorAUIChanged = true;
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
