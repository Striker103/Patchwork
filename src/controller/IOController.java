package controller;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import model.CheckUtil;
import model.Game;
import model.Matrix;
import model.Patch;
import view.aui.ErrorAUI;
import view.aui.LoadGameAUI;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class IOController {

	private final MainController mainController;

	private final ErrorAUI errorAUI;

	private final LoadGameAUI loadGameAUI;

	//TODO
	private final String pathToCSV = "";

	/**
	 * Constructor that sets the mainController and all AUIs
	 * @param mainController The controller that knows all other controllers
	 * @param errorAUI the errorAUI
	 */
	public IOController(MainController mainController, ErrorAUI errorAUI, LoadGameAUI loadGameAUI){
		this.mainController = mainController;
		this.errorAUI = errorAUI;
		this.loadGameAUI = loadGameAUI;
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

	/**
	 * Loads all games from a directory
	 * @param file directory
	 */
	public void loadGame(File file) {

		if (!file.isDirectory() || !file.canWrite()){
			errorAUI.showError("Invalid file");
			return;
		}

		File[] files = file.listFiles();

		if (files != null) {
			List<Game> games = new LinkedList<>();

			Gson gson = new Gson();

			for (File possibleGame : files) {

				if (possibleGame.canWrite() && possibleGame.isFile()){
					games.add(readGame(possibleGame));
				}

			}

			loadGameAUI.loadGame(games);
		} else {
			errorAUI.showError("IOException occurred.");
		}

	}

	/**
	 * Imports patches from csv file
	 * @param  file csv file
	 * @return list of patches
	 */
	public List<Patch> importCSV(File file) {

		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(";");
				records.add(Arrays.asList(values));
			}
		} catch (IOException e){
			errorAUI.showError("IOException occurred.");
			return null;
		}

		List<Patch> patchList = new LinkedList<>();

		for (int i = 1; i < records.size(); i++) {
			patchList.add(generatePatch(records.get(i),i));
		}

		return patchList;
	}

	/**
	 * Imports default patches and shuffles them
	 * @return shuffled default patches
	 */
	public List<Patch> importCSV() {
		List<Patch> patchList = importCSV(new File(pathToCSV));

		Collections.shuffle(patchList);

		return patchList;
	}

	public void exportGameResult() {
		//TODO
	}

	private Game readGame(File file){

		try {
			String json = Files.readString(file.toPath());

			Gson gson = new Gson();
			return gson.fromJson(json, Game.class);

		} catch (IOException exception) {
			errorAUI.showError("IOError occurred.");
			return null;
		} catch (JsonSyntaxException exception){
			return null;
		}
	}






	private Patch generatePatch(List<String> line, int patchID){

		if (line.get(0).length() != 15){
			throw  new IllegalArgumentException("Invalid csv.");
		}

		boolean[][] shape;

		try {
			shape = buildShape(line.get(0));
		} catch (IllegalArgumentException e) {
			errorAUI.showError(e.getMessage());
			return null;
		}

		int buttonCost = Integer.parseInt(line.get(1));
		int time = Integer.parseInt(line.get(2));
		int buttonIncome = Integer.parseInt(line.get(3));

		return new Patch(patchID, buttonIncome, buttonCost, new Matrix(shape), time);

	}

	private boolean[][] buildShape(String csv){

		boolean[][] shape = new boolean[5][5];

		for (int i = 0; i < csv.length(); i++) {

			boolean square;

			if (csv.charAt(i) == 'X'){
				square = true;
			} else if (csv.charAt(i) == '-'){
				square = false;
			} else {
				throw new IllegalArgumentException("Invalid csv.");
			}

			shape[i / 5][ i % 5] = square;

		}

		return shape;
	}

}
