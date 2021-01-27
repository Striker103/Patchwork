package controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.*;
import view.aui.ErrorAUI;
import view.aui.LoadGameAUI;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * @author Alexandra Latys
 */
public class IOController {

	private final MainController mainController;

	private ErrorAUI errorAUI;

	private LoadGameAUI loadGameAUI;

	/**
	 * true if errorAUI is set
	 */
	private boolean errorAUIChanged = false;

	/**
	 * true if errorAUI is set
	 */
	private boolean loadGameAUIChanged = false;

	private final String pathToCSV = "CSV/patchwork-pieces.csv";

	/**
	 * Constructor that sets the mainController
	 *
	 * @param mainController The controller that knows all other controllers
	 */
	public IOController(MainController mainController){
		this.mainController = mainController;
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

		if (file.isDirectory() || !file.canWrite()){
			errorAUI.showError("Invalid file.");
			return null;
		}

		List<List<String>> records = new ArrayList<>();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] values = line.split(";");
				records.add(Arrays.asList(values));
			}
		} catch (IOException e){
			errorAUI.showError("IOException occurred.");
			return null;
		}

		List<Patch> patchList = new LinkedList<>();

		for (int i = 1; i < records.size(); i++) {
			patchList.add(generatePatch(records.get(i), i));
		}

		return patchList;
	}

	/**
	 * Imports default patches
	 * @return patches
	 */
	public List<Patch> importCSVNotShuffled() {

		File file = new File(pathToCSV);

		if (file.isDirectory() || !file.canWrite()){
			errorAUI.showError("Invalid file.");
			return null;
		}

		List<Patch> patchList = importCSV(file);


		return patchList;
	}

	/**
	 * Imports default patches and shuffles them
	 * @return shuffled default patches
	 */
	public List<Patch> importCSV() {

		File file = new File(pathToCSV);

		if (file.isDirectory() || !file.canWrite()){
			errorAUI.showError("Invalid file.");
			return null;
		}

		List<Patch> patchList = importCSV(file);

		Collections.shuffle(patchList);

		return patchList;
	}

	/**
	 * Exports game results to specified path
	 * @param file path
	 */
	public void exportGameResult(File file) {

		try {
			CheckUtil.assertNonNull(file);
		} catch (IllegalArgumentException e) {
			errorAUI.showError("File is null.");
			return;
		}

		if (file.isDirectory() || !file.canWrite()){
			errorAUI.showError("File is invalid.");
			return;
		}

		Player player1 = mainController.getGame().getCurrentGameState().getPlayer1();
		Player player2 = mainController.getGame().getCurrentGameState().getPlayer2();


		try {
			CheckUtil.assertNonNull(player1, player2);
		} catch (IllegalArgumentException e) {
			errorAUI.showError("Game is broken.");
			return;
		}

		//Check if game is finished
		final int lastTimeBoardIndex = 53;
		if (player1.getBoardPosition() != lastTimeBoardIndex || player2.getBoardPosition() != lastTimeBoardIndex){
			errorAUI.showError("Game is not finished.");
			return;
		}

		try {
			Document document = new Document();

			FileOutputStream fileOutputStream = new FileOutputStream(file);

			PdfWriter.getInstance(document, fileOutputStream);

			document.open();

			Paragraph paragraph = new Paragraph();
			paragraph.add("Results");

			document.add(paragraph);
			document.add(getWinner(player1,player2));
			document.add(getPlayerInformation(player1));
			document.add(getGrid(player1));
			document.add(getPlayerInformation(player2));
			document.add(getGrid(player2));

			document.close();
			fileOutputStream.close();
		} catch (DocumentException | IOException  e) {
			errorAUI.showError("IOException occurred.");
		}

	}

	private Paragraph getWinner(Player player1, Player player2){
		Paragraph paragraph = new Paragraph();

		String winnerName;

		if (player1.getScore().getValue() == player2.getScore().getValue())
			winnerName = "Tie";
		else if (player1.getScore().getValue() > player2.getScore().getValue())
			winnerName = player1.getName();
		else
			winnerName = player2.getName();

		paragraph.add("Winner: " + winnerName);

		return paragraph;
	}

	private Paragraph getPlayerInformation(Player player){
		Paragraph paragraph = new Paragraph();

		String text = "Name: " + player.getName() +
				"\nPlayer Type: " + player.getPlayerType().toString() +
				"\nScore: " + player.getScore().getValue() +
				"\nNumber of Patches: " + player.getQuiltBoard().getPatches().size() +
				"\nMoney: " + player.getMoney();

		paragraph.add(text);

		return paragraph;
	}

	private PdfPTable getGrid(Player player){

		PdfPTable pdfPTable = new PdfPTable(9);

		for (int i = 0 ; i < 81; i++){
			pdfPTable.addCell(String.valueOf(player.getQuiltBoard().getPatchBoard().get(i % 9, i / 9)));
		}

		return pdfPTable;

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

	private Patch generatePatch(List<String> line, int id){

		final int matrixLength = 15;

		if (line.get(0).length() != matrixLength){
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

		Matrix shapeMatrix = new Matrix(shape);

		return new Patch(id, buttonIncome, buttonCost, shapeMatrix, time);

	}

	private boolean[][] buildShape(String csv){

		final char fill = 'X';
		final char empty = '-';

		boolean[][] shape = new boolean[3][5];

		for (int i = 0; i < csv.length(); i++) {

			boolean square;

			if (csv.charAt(i) == fill){
				square = true;
			} else if (csv.charAt(i) == empty){
				square = false;
			} else {
				throw new IllegalArgumentException("Invalid csv.");
			}

			shape[i / 5][i % 5] = square;

		}

		return shape;
	}

	/**
	 * set the errorAUI
	 * @param errorAUI the errorAUI
	 */
	public void setErrorAUI(ErrorAUI errorAUI) {
		if(this.errorAUIChanged) throw new IllegalStateException("errorAUI was already set");
		this.errorAUI = errorAUI;
		this.errorAUIChanged = true;
	}

	/**
	 * set the loadGameAUI
	 * @param loadGameAUI the loadGameAUI
	 */
	public void setLoadGameAUI(LoadGameAUI loadGameAUI) {
		if(this.loadGameAUIChanged) throw new IllegalStateException("loadGameAUI was already set");
		this.loadGameAUI = loadGameAUI;
		this.loadGameAUIChanged = true;
	}
}
