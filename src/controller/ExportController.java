package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.CheckUtil;
import model.Player;
import view.aui.ErrorAUI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

/**
 * @author Alexandra Latys
 */
public class ExportController {

    private ErrorAUI errorAUI = null;
    private MainController mainController;

    private final HashMap<Integer, BaseColor> colorHashMap = new HashMap<>();

    /**
     * Constructor that sets the mainController
     *
     * @param mainController The controller that knows all other controllers
     */
    public ExportController(MainController mainController) {
        this.mainController = mainController;
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

        if (file.isDirectory() || (file.exists() && !file.canWrite())){
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
        final int lastTimeBoardIndex = mainController.getGame().getCurrentGameState().getTimeBoard().length;
        if (player1.getBoardPosition() != lastTimeBoardIndex || player2.getBoardPosition() != lastTimeBoardIndex){
            errorAUI.showError("Game is not finished.");
            return;
        }

        try {
            Document document = new Document();

            FileOutputStream fileOutputStream = new FileOutputStream(file);

            PdfWriter.getInstance(document, fileOutputStream);

            document.open();

            Font font = new Font(Font.FontFamily.HELVETICA, 25.0f, Font.BOLD, BaseColor.BLACK);
            Chunk chunk = new Chunk("Results", font);

            Paragraph paragraph = new Paragraph(chunk);

            paragraph.setAlignment(Element.ALIGN_CENTER);

            document.add(paragraph);
            document.add(getWinner(player1,player2));
            document.add(getPlayerInformation(player1));
            document.add(getGrid(player1));
            document.newPage();
            document.add(getPlayerInformation(player2));
            document.add(getGrid(player2));

            document.close();
            fileOutputStream.close();
        } catch (DocumentException | IOException e) {
            errorAUI.showError("IOException occurred.");
        }

    }


    private Paragraph getWinner(Player player1, Player player2){

        String winnerName;

        if (player1.getScore().getValue() == player2.getScore().getValue())
            winnerName = "Tie";
        else if (player1.getScore().getValue() > player2.getScore().getValue())
            winnerName = player1.getName();
        else
            winnerName = player2.getName();


        Font font = new Font(Font.FontFamily.HELVETICA, 20.0f, Font.BOLD, BaseColor.BLACK);
        Chunk chunk = new Chunk("Winner: " + winnerName + "\n", font);


        Paragraph paragraph = new Paragraph(chunk);

        paragraph.add(Chunk.NEWLINE);

        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }


    private Paragraph getPlayerInformation(Player player){
        Paragraph paragraph = new Paragraph();

        int numberOfSpecialPatches = player.getQuiltBoard().getPatchBoard().count(Integer.MAX_VALUE);

        String text = "Name: " + player.getName() +
                "\nPlayer Type: " + player.getPlayerType().toString() +
                "\nScore: " + player.getScore().getValue() +
                "\nNumber of normal Patches: " + player.getQuiltBoard().getPatches().size() +
                "\nNumber of special Patches: " + numberOfSpecialPatches +
                "\nMoney: " + player.getMoney() + "\n";

        Font font = new Font(Font.FontFamily.HELVETICA, 15.0f, Font.NORMAL, BaseColor.BLACK);
        Chunk chunk = new Chunk(text, font);

        paragraph.add(chunk);
        paragraph.setAlignment(Element.ALIGN_CENTER);

        paragraph.add(Chunk.NEWLINE);

        return paragraph;
    }

    private PdfPTable getGrid(Player player){
        colorHashMap.clear();
        colorHashMap.put(0, new BaseColor(255,255,255));

        PdfPTable pdfPTable = new PdfPTable(9);

        for (int i = 0 ; i < 81; i++){

            PdfPCell cell = new PdfPCell();
            cell.setColspan(1);
            cell.setFixedHeight(50.0F);
            BaseColor color = generateColor(player.getQuiltBoard().getPatchBoard().get(i / 9, i % 9));
            cell.setBackgroundColor(color);
            pdfPTable.addCell(cell);
        }

        return pdfPTable;

    }

    private BaseColor generateColor(int value){
        BaseColor color = colorHashMap.get(value);

        if (color == null){
            color = new BaseColor(getRandomColorValue(), getRandomColorValue(), getRandomColorValue());
            colorHashMap.put(value, color);
        }

        return color;
    }

    private int getRandomColorValue(){
        return new Random().nextInt(256);
    }

    /**
     * set the errorAUI
     * @param errorAUI the errorAUI
     */
    public void setErrorAUI(ErrorAUI errorAUI) {

        if (this.errorAUI == null)
            this.errorAUI = errorAUI;
        else
            throw new IllegalStateException("errorAUI was already set");

    }
}
