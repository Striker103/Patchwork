package view.control;

import controller.GameController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import model.*;
import view.PatchMap;
import view.PatchView;
import view.aui.TurnAUI;


public class GameScreenViewController implements TurnAUI {

    public Label player1Name;
    public Label player2Name;
    public Label player1Buttons;
    public Label player2Buttons;
    private MainViewController mainViewController;
    private String firstPlayerName;
    private Scene ownScene;
    private PatchView activePatchView;
    private TimeToken timeToken1;
    private TimeToken timeToken2;
    private TimeToken activeTimeToken;
    private List<PatchView> patchViews;
    private List<Patch> patches;
    private Game game;
    private List<PatchView> specialPatches;
    private int specialPatchIndex = 0;
    private boolean isPlaced = true;

    @FXML
    Pane pane;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView imageView1, imageView2, imageView3;

    @FXML
    private Label cost1, time1, cost2, time2, cost3, time3;

    @FXML
    private ListView<ImageView> patchListView;


    public GameScreenViewController(){
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void initGame(){
        loadTimeBoard();

        game = mainViewController.getMainController().getGame();
        patches = game.getCurrentGameState().getPatches();

        player1Name.setText(game.getCurrentGameState().getPlayer1().getName());
        player2Name.setText(game.getCurrentGameState().getPlayer2().getName());
        updateMoney();
        refreshList();
        showSelectablePatches();
        try {
            loadPatches();
            loadSpecialPatches();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        firstPlayerName = getFirstPlayerName();
        int[][] arr = new int[3][5];
        arr[0][0] = 1;
        Matrix shape = new Matrix(arr);

        specialPatches = new ArrayList<>();
        for(int i = 0; i< 5; i++)
        {
            Patch patch = new Patch(999 + i, 0 , 0 , shape ,0);
            specialPatches.add(new PatchView(patch));
        }


    }

    private String getFirstPlayerName(){
        return mainViewController.getMainController().getGameController().getNextPlayer().getName();
    }

    private boolean isFirstPlayer(){
        Player nextPlayer = mainViewController.getMainController().getGameController().getNextPlayer();
        return nextPlayer.getName().equals(firstPlayerName);
    }

    public void updateMoney()
    {
        player1Buttons.setText("Buttons: " + game.getCurrentGameState().getPlayer1().getMoney());
        player2Buttons.setText("Buttons: " + game.getCurrentGameState().getPlayer2().getMoney());
    }

    public void refreshList()
    {

        patchListView.getItems().clear();

        patchViews = new ArrayList<>();
        patches = game.getCurrentGameState().getPatches();

        for(Patch patch : patches)
        patchViews.add(new PatchView(patch));

        try {
            loadPatches();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        showSelectablePatches();
        activePatchView = patchViews.get(0);
        if(!isPlaced)
            placeSpecialTile();
    }

    public void setOwnScene(Scene scene)  {
        this.ownScene = scene;
    }

    public void showScene(){
        mainViewController.setCurrentScene(ownScene);
        mainViewController.showCurrentScene();
    }

    public void loadTimeBoard(){
        ImageView timeBoard = new ImageView();
        try {
            timeBoard.setImage(new Image(this.getClass().getResource("/view/images/TimeBoard/TimeBoard.png").toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        timeBoard.setFitWidth(360); //was 270 x 270
        timeBoard.setFitHeight(360);
        timeBoard.setX(445);
        timeBoard.setY(40);
        this.pane.getChildren().add(timeBoard);

        timeToken1 = new TimeToken(1);
        timeToken1.setX(558);
        timeToken1.setY(82);
        timeToken1.setFitWidth(20);
        timeToken1.setFitHeight(20);
        pane.getChildren().add(timeToken1);

        timeToken2 = new TimeToken(2);
        timeToken2.setX(560);
        timeToken2.setY(82);
        timeToken2.setFitWidth(20);
        timeToken2.setFitHeight(20);
        activeTimeToken = timeToken2;
        pane.getChildren().add(activeTimeToken);

    }

    //TODO when a patch which was already clicked is clicked again there are warnings
    public void loadPatches() throws FileNotFoundException {
        for(Patch patch : patches)
        {
            String path = PatchMap.getInstance().getImagePath(patch);
            imageView = new ImageView(new Image(new FileInputStream("src/" + path)));

            patchListView.getItems().add(imageView);
            Matrix shape = patch.getShape();
            Matrix trim = shape.trim();
            int height = trim.getRows();
            int width = trim.getColumns();
            imageView.setFitHeight(height * 30);
            imageView.setFitWidth(width * 30);
        }
    }

    //TODO behaviour of list if less then three patches are in the list view
    public void showSelectablePatches(){
        Patch patch;
        Matrix shape;
        Matrix trim;
        int height;
        int width;
        if(patches.size() >= 1){
            imageView1.setImage(patchViews.get(0).getImage());
            patch = patches.get(0);
            shape = patch.getShape();
            trim = shape.trim();
            height = trim.getRows();
            width = trim.getColumns();
            imageView1.setFitHeight(height * 30);
            imageView1.setFitWidth(width * 30);
            cost1.setText("Cost: " + patch.getButtonsCost());
            time1.setText("Time: " + patch.getTime());
        }
        if(patches.size() >= 2){
            imageView2.setImage(patchViews.get(1).getImage());
            patch = patches.get(1);
            shape = patch.getShape();
            trim = shape.trim();
            height = trim.getRows();
            width = trim.getColumns();
            imageView2.setFitHeight(height * 30);
            imageView2.setFitWidth(width * 30);
            cost2.setText("Cost: " + patch.getButtonsCost());
            time2.setText("Time: " + patch.getTime());
        }

        if(patches.size() >= 3){
            imageView3.setImage(patchViews.get(2).getImage());
            patch = patches.get(2);
            shape = patch.getShape();
            trim = shape.trim();
            height = trim.getRows();
            width = trim.getColumns();
            imageView3.setFitHeight(height * 30);
            imageView3.setFitWidth(width * 30);
            cost3.setText("Cost: " + patch.getButtonsCost());
            time3.setText("Time: " + patch.getTime());
        }
    }

    public void loadSpecialPatches() throws FileNotFoundException {
        String path = "src/view/images/Patches/SpecialPatch.png";
        for(int i = 1; i < 6; i++){
            imageView = new ImageView(new Image(new FileInputStream(path)));
            if(i == 1){
                imageView.setFitHeight(25);
                imageView.setFitWidth(25);
                imageView.setX(490);
                imageView.setY(290);
                pane.getChildren().add(imageView);
            }
            if(i == 2){
                imageView.setFitHeight(25);
                imageView.setFitWidth(25);
                imageView.setX(562);
                imageView.setY(115);
                pane.getChildren().add(imageView);
            }
            if(i == 3){
                imageView.setFitHeight(25);
                imageView.setFitWidth(25);
                imageView.setX(703);
                imageView.setY(220);
                pane.getChildren().add(imageView);
            }
            if(i == 4){
                imageView.setFitHeight(25);
                imageView.setFitWidth(25);
                imageView.setX(579);
                imageView.setY(151);
                pane.getChildren().add(imageView);
            }
            if(i == 5){
                imageView.setFitHeight(25);
                imageView.setFitWidth(25);
                imageView.setX(597);
                imageView.setY(255);
                pane.getChildren().add(imageView);
            }
        }
    }

    public void PaneDragged() {
    }

    @Override
    public void triggerPlayerTurn() {
        refreshList();
        updateMoney();
    }

    public void placeSpecialTile(){
        boolean firstPlayer = isFirstPlayer();
        if(firstPlayer){
            specialPatches.get(specialPatchIndex).setX(1040);
        }else{
            specialPatches.get(specialPatchIndex).setX(150);
        }
        specialPatches.get(specialPatchIndex).setY(150);
        pane.getChildren().add(specialPatches.get(specialPatchIndex));
        activePatchView = specialPatches.get(specialPatchIndex);
        specialPatchIndex++;
    }

    @Override
    public void trigger1x1Placement() {
        isPlaced = false;
    }

    public void reTriggerPatchPlacement() {
        Alert alarm = new Alert(Alert.AlertType.ERROR);
        alarm.setTitle("Error");
        alarm.setContentText("there is already a patch on this position");
        alarm.showAndWait();

    }

    @Override
    public void updatePatches() {
        refreshList();
        updateMoney();
    }

    @Override
    public void moveToken(String name, int time) {
        if(name.equals(player1Name.getText())){
            timeToken1.moveToken(time);
        }else{
            timeToken2.moveToken(time);
        }

    }


    private class TimeToken extends ImageView {
        private int firstPosX = 0;
        private int firstPosY = 0;
        private int lastPosX = 14;
        private int lastPosY = 14;

        private int positionOnBoard = 0;
        private int currentPositionX = 4;
        private int currentPositionY = 0;

        private static final double STEPPING = 36;
        private int verticalDirection = 1; // -1 = left, 1 = right
        private int horizontalDirection = 1; // -1 = up, 1 = down


        /**
         * Constructor for a time token
         * @param id the id of the token (in our case either 1 or 2)
         */
        private TimeToken(int id) {
            try {
                this.setImage(new Image(this.getClass().getResource("/view/images/TimeTokens/TimeToken"+ id + ".png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            pane.setPrefSize(1280, 720);
            this.setFitWidth(200);
            this.setFitHeight(200);

        }

        /**
         * Moves time token with a specified number of steps
         * @param steps the number of steps to take
         */
        void moveToken(int steps) {
            for (int i = 0; i < steps; i++) {
                if(this.positionOnBoard >= 53){
                    System.out.println("Goal reached!");
                    break;
                }
                moveToken();
            }
        }

        /**
         * Moves the time token one step at a time
         */
        void moveToken(){
            if(verticalDirection == 1 && currentPositionX +2 <= this.lastPosX){
                this.setX(this.getX() + STEPPING * verticalDirection);
                currentPositionX+=2;
                positionOnBoard++;
                if(Math.abs(this.currentPositionX - this.lastPosX) <= 1 && Math.abs(this.currentPositionY - this.firstPosY) <= 1) {
                    this.horizontalDirection = 1;
                    this.firstPosY += 2;
                }
            }
            else if(verticalDirection == -1 && currentPositionX -2 >= this.firstPosX) {
                this.setX(this.getX() + STEPPING * verticalDirection);
                currentPositionX -= 2;
                positionOnBoard++;
                if (Math.abs(this.currentPositionX - this.firstPosX) <= 1 && Math.abs(this.currentPositionY - this.lastPosY) <= 1) {
                    this.horizontalDirection = -1;
                    this.lastPosY -= 2;
                }
            }
            else if(horizontalDirection == 1 && currentPositionY +2 <= this.lastPosY){
                this.setY(this.getY() + STEPPING * horizontalDirection);
                currentPositionY+=2;
                positionOnBoard++;
                if(Math.abs(this.currentPositionX - this.lastPosX) <= 1 && Math.abs(this.currentPositionY - this.lastPosY) <= 1) {
                    this.verticalDirection = -1;
                    this.lastPosX -= 2;
                }
            }
            else if(horizontalDirection == -1 && currentPositionY -2 >= this.firstPosY){
                this.setY(this.getY() + STEPPING * horizontalDirection);
                currentPositionY-=2;
                positionOnBoard++;
                if(Math.abs(this.currentPositionX - this.firstPosX) <= 1 && Math.abs(this.currentPositionY - this.firstPosY) <= 1) {
                    this.verticalDirection = 1;
                    this.firstPosX += 2;
                }
            }
            if(positionOnBoard == 19|| positionOnBoard == 21){
                currentPositionY--;
                this.setY(this.getY() - 0.25 * STEPPING);
            }
            if(positionOnBoard == 20 ){
                this.setY(this.getY() - 0.5 * STEPPING);
            }
            if(positionOnBoard == 25 || positionOnBoard == 27 ){
                currentPositionX++;
                this.setX(this.getX() + 0.25 * STEPPING);
            }
            if(positionOnBoard == 26 ){
                this.setX(this.getX() + 0.5 * STEPPING);
            }

            if(positionOnBoard == 31 || positionOnBoard == 33 ){
                currentPositionY++;
                this.setY(this.getY() + 0.25 * STEPPING);
            }
            if(positionOnBoard == 32 ){
                this.setY(this.getY() + 0.5 * STEPPING);
            }

            if(positionOnBoard == 44 || positionOnBoard == 46 ){
                currentPositionX++;
                this.setX(this.getX() + 0.25 * STEPPING);
            }
            if(positionOnBoard == 45 ){
                this.setX(this.getX() + 0.5 * STEPPING);
            }

            if(positionOnBoard == 49 || positionOnBoard == 51 ){
                currentPositionX--;
                this.setX(this.getX() - 0.25 * STEPPING);
            }
            if(positionOnBoard == 50 ){
                this.setX(this.getX() - 0.5 * STEPPING);
            }
        }
    }
    @FXML
    public void onUndoAction() {
    }

    @FXML
    public void onPauseAction() {
        mainViewController.getPauseGameViewController().showScene();
    }

    @FXML
    public void onPassAction() {
    }

    @FXML
    public void onRedoAction() {
    }

    @FXML
    public void onHintAction() {
    }

    @FXML
    public void onChoose1Action() {
        if((activePatchView.getHeight() == 1 && activePatchView.getWidth() == 1))
            return;
        removePatches();
        if(patches.size() == 0){
            return;
        }
        boolean firstPlayer = isFirstPlayer();
        PatchView patchView = patchViews.get(0);
        pane.getChildren().add(patchView);
        if(firstPlayer){
            patchView.setX(150);
        }else{
            patchView.setX(1040);
        }
        patchView.setY(150);

        if(!patchView.isVisible()){
            patchView.setVisible(true);
        }
        activePatchView = patchView;
    }

    @FXML
    public void onChoose2Action() {
        if((activePatchView.getHeight() == 1 && activePatchView.getWidth() == 1))
            return;
        removePatches();
        if(patches.size() <= 1){
            return;
        }
        boolean firstPlayer = isFirstPlayer();
        PatchView patchView = patchViews.get(1);
        pane.getChildren().add(patchView);
        if(firstPlayer){
            patchView.setX(150);
        }else{
            patchView.setX(1040);
        }
        patchView.setY(150);
        if(!patchView.isVisible()){
            patchView.setVisible(true);
        }
        activePatchView = patchView;
    }

    @FXML
    public void onChoose3Action() {
        if((activePatchView.getHeight() == 1 && activePatchView.getWidth() == 1))
            return;
        removePatches();
        if(patches.size() <= 2){
            return;
        }
        boolean firstPlayer = isFirstPlayer();
        PatchView patchView = patchViews.get(2);
        pane.getChildren().add(patchView);
        if(firstPlayer){
            patchView.setX(150);
        }else{
            patchView.setX(1040);
        }
        patchView.setY(150);
        if (!patchView.isVisible()) {
            patchView.setVisible(true);
        }
        activePatchView = patchView;
    }

    public void removePatches(){
        pane.getChildren().remove(patchViews.get(0));
        pane.getChildren().remove(patchViews.get(1));
        pane.getChildren().remove(patchViews.get(2));
    }

    /**
     * changes position and rotation of the activePatch
     *
     * @param keyEvent the pressed kay
     */
    public void handleKeyPressed(KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.NUMPAD5){
            if(activePatchView.moveIsLegit('w')){
                activePatchView.setFirstPlayer(isFirstPlayer());
                activePatchView.moveUp();
            }
        }else if(keyEvent.getCode() == KeyCode.S  || keyEvent.getCode() == KeyCode.NUMPAD2){
            if(activePatchView.moveIsLegit('s')){
                activePatchView.setFirstPlayer(isFirstPlayer());
                activePatchView.moveDown();
            }
        }
        else if(keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.NUMPAD1 ){
            if(activePatchView.moveIsLegit('a')){
                activePatchView.setFirstPlayer(isFirstPlayer());
                activePatchView.moveLeft();
            }
        }
        else if(keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.NUMPAD3){
            if(activePatchView.moveIsLegit('d')){
                activePatchView.setFirstPlayer(isFirstPlayer());
                activePatchView.moveRight();
            }
        }
        else if(keyEvent.getCode() == KeyCode.E || keyEvent.getCode() == KeyCode.NUMPAD6){
            if(activePatchView.rotationIsLegit()){
                activePatchView.setFirstPlayer(isFirstPlayer());
                activePatchView.rotate();
            }else{
                System.out.println("please move away from the corner a little bit");
            }
        }
        else if(keyEvent.getCode() == KeyCode.Q || keyEvent.getCode() == KeyCode.NUMPAD4) {
            activePatchView.setFirstPlayer(isFirstPlayer());
            activePatchView.flip();
        }else if(keyEvent.getCode() == KeyCode.R || keyEvent.getCode() == KeyCode.NUMPAD7){
            if(!isPlaced)
                return;
            removePatches();
            GameController gameController = mainViewController.getMainController().getGameController();
            gameController.advance();
        }
        else if(keyEvent.getCode() == KeyCode.F || keyEvent.getCode() == KeyCode.NUMPAD9){
            if(!isPlaced)
                return;
            GameController gameController = mainViewController.getMainController().getGameController();
            gameController.takePatch(activePatchView.getPatch(), activePatchView.readyToGo(), activePatchView.getRotation(), activePatchView.getFlipped());


        }else if(keyEvent.getCode() == KeyCode.C){ //only for debugging
            Matrix ready = activePatchView.readyToGo();
            ready.print();
            System.out.println();
        }else if(keyEvent.getCode() == KeyCode.T){ //just to test the movement of the time token on the time board
            activeTimeToken.moveToken(
            );

        }else if(keyEvent.getCode() == KeyCode.N){ //only for debugging
            System.out.println("current Player:" + mainViewController.getMainController().getGameController().getNextPlayer().getName());
            Matrix firstPlayerBoard = game.getCurrentGameState().getPlayer1().getQuiltBoard().getPatchBoard();
            firstPlayerBoard.print();
            System.out.println();
        }else if(keyEvent.getCode() == KeyCode.M){ //only for debugging
            System.out.println("current Player:" + mainViewController.getMainController().getGameController().getNextPlayer().getName());
            Matrix secondPlayerBoard = game.getCurrentGameState().getPlayer2().getQuiltBoard().getPatchBoard();
            secondPlayerBoard.print();
            System.out.println();
        }else if(keyEvent.getCode() == KeyCode.X || keyEvent.getCode() == KeyCode.NUMPAD8){
            if(!(activePatchView.getHeight() == 1 && activePatchView.getWidth() == 1))
                return;
            activePatchView.setFirstPlayer(isFirstPlayer());
            Player currentPlayer = mainViewController.getMainController().getGameController().getNotMovingPlayer();
            isPlaced = true;
            GameController gameController = mainViewController.getMainController().getGameController();
            boolean placed = gameController.place1x1Patch(activePatchView.getPosX() -2, activePatchView.getPosY()-2, currentPlayer);
            if(placed){
                refreshList();
                System.out.println("1x1 Patch placed");
            }else{
                System.out.println("there is already a patch");
            }
        }
    }
}
