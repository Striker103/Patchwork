package controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.CheckUtil;
import model.Player;
import model.PlayerType;
import model.Score;
import view.aui.ErrorAUI;
import view.aui.HighscoreAUI;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.LinkedList;

public class HighScoreController {

	private HighscoreAUI highscoreAUI;

	private MainController mainController;

	private ErrorAUI errorAUI;

	private File HIGHSCOREFILE;

	public HighScoreController(File highscoreFile) {


		CheckUtil.assertNonNull(highscoreFile);

		if (highscoreFile.exists() && (!highscoreFile.isFile() && !highscoreFile.canWrite())){
			throw new IllegalArgumentException("File is invalid.");
		}

		HIGHSCOREFILE = highscoreFile;
	}

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


	public void updateScore(Player player){

	}



	public void saveScores() {

//		if(!mainController.hasGame())
//			errorAUI.showError("No game existing.");
//
//		Player player1 = mainController.getGame().getGameStates().get(mainController.getGame().getCurrentGameState()).getPlayer1();
//		Player player2 = mainController.getGame().getGameStates().get(mainController.getGame().getCurrentGameState()).getPlayer2();
//
//		if (player1.getPlayerType() == PlayerType.HUMAN)
//			savePlayer(player1);
//
//		if (player2.getPlayerType() == PlayerType.HUMAN)
//			savePlayer(player2);

	}

	public void clearHighscores() {
		try {
			new PrintWriter(HIGHSCOREFILE).close();
		} catch (FileNotFoundException e) {
			errorAUI.showError("IOError occurred.");
		}
	}

	public void showHighScores(){
		highscoreAUI.showHighscores(readHighscores());
	}

	private void savePlayer(Player player){

		CheckUtil.assertNonNull(player);

		Score score = calculateScore(player);

		LinkedList<Score> scores = readHighscores();
		scores.add(score);

//		Collections.sort(scores); //TODO comparator

		clearHighscores();
		writeHighscores(scores);
	}

	//TODO
	private Score calculateScore(Player player) {
		return null;
	}

	private void writeHighscores(LinkedList<Score> scores){

		CheckUtil.assertNonNull(scores);

		Gson gson = new Gson();
		String json = gson.toJson(scores);

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(HIGHSCOREFILE));
			writer.write(json);

			writer.close();
		} catch (IOException exception) {
			errorAUI.showError("IOException occurred.");
		}
	}

	private LinkedList<Score> readHighscores() {

		try {
			String json = Files.readString(HIGHSCOREFILE.toPath());

			Gson gson = new Gson();
			return gson.fromJson(json, new TypeToken<LinkedList<Score>>(){}.getType());

		} catch (IOException exception) {
			errorAUI.showError("IOError occurred.");
			return null;
		}

	}

	public void setErrorAUI(ErrorAUI errorAUI){
		if (this.errorAUI == null)
			this.errorAUI = errorAUI;
		else
			throw new IllegalStateException("ErrorAUI is already set");
	}

	public void setHighscoreAUI(HighscoreAUI highscoreAUI){
		if (this.highscoreAUI == null)
			this.highscoreAUI = highscoreAUI;
		else
			throw new IllegalStateException("HighscoreAUI is already set");
	}

}
