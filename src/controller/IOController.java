package controller;


import com.google.gson.Gson;
import model.CheckUtil;
import model.Game;
import model.Patch;
import view.aui.ErrorAUI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class IOController {

	private final MainController mainController;

	private final ErrorAUI errorAUI;

	/**
	 * Constructor that sets the mainController and all AUIs
	 * @param mainController The controller that knows all other controllers
	 * @param errorAUI the errorAUI
	 */
	public IOController(MainController mainController, ErrorAUI errorAUI){
		this.mainController = mainController;
		this.errorAUI = errorAUI;
	}

	/**
	 * Saves the game to the specified file
	 * @param file file
	 */
	public void saveGame(File file) {
		Game game = mainController.getGame();

		CheckUtil.assertNonNull(game, file);

		if (file.isDirectory() || !file.canWrite()) {
			errorAUI.showError("File is invalid");
			return;
		}

		Gson gson = new Gson();
		String json = gson.toJson(game);

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(json);

			writer.close();
		} catch (IOException exception) {
			errorAUI.showError("IOException occurred.");
		}

	}

	public void loadGame() {

	}

	public void exportGameResult() {

	}

	public List<Patch> importCSV(File file) {

		//		List<List<String>> records = new ArrayList<>();
//		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				String[] values = line.split(COMMA_DELIMITER);
//				records.add(Arrays.asList(values));
//			}
//		} catch (IOException e){
//			errorAUI.showError("IOException occurred.");
//			return null;
//		}
		return null;
	}

	public List<Patch> importCSV() {
		return null;
	}

}
