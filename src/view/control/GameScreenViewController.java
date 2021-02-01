package view.control;

import controller.GameController;
import controller.HighScoreController;
import controller.IOController;
import controller.UndoRedoController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import model.*;
import view.PatchMap;
import view.PatchView;
import view.aui.LogAUI;
import view.aui.TurnAUI;


public class GameScreenViewController implements TurnAUI , LogAUI {

    public Label player1Name;
    public Label player2Name;
    public Label player1Buttons;
    public Label player2Buttons;
    public Label player1Score;
    public Label player2Score;
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
    private List<PatchView> specialPatchesOnBoard;
    private int specialPatchIndex = 0;
    private boolean isPlaced = true;
    private List<PatchView> listToClear;
    private List<PatchView> listToClearGUI;
    private List<PatchView> listInOrder;

    @FXML
    Pane pane;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView imageView1, imageView2, imageView3;

    @FXML
    private Label cost1, time1, cost2, time2, cost3, time3;

    @FXML
    private ListView<Tuple<ImageView, Patch>> patchListView;

    @FXML
    private ListView<String> logList;


    public GameScreenViewController(){
    }

    public void initListView(){

        patchListView.setCellFactory(listView -> new ListCell<>() {
            public void updateItem(Tuple<ImageView, Patch> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                    setTooltip(null);
                } else {
                    String information = "Cost: " + item.getSecond().getButtonsCost() + "\nIncome: " + item.getSecond().getButtonIncome();

                    Tooltip tooltip = new Tooltip();
                    tooltip.setShowDelay(Duration.seconds(0.2));
                    tooltip.setText(information);
                    setGraphic(item.getFirst());
                    setText(information);
                    setTooltip(tooltip);

                }
            }

        });
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void initGame() {
        int[][] arr = new int[3][5];
        arr[0][0] = 1;
        Matrix shape = new Matrix(arr);
        specialPatchesOnBoard = new ArrayList<>();
        specialPatches = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Patch patch = new Patch(999 + i, 0, 0, shape, 0);
            specialPatches.add(new PatchView(patch));
            specialPatchesOnBoard.add(new PatchView(patch));
        }

        loadTimeBoard();

        game = mainViewController.getMainController().getGame();
        patches = game.getCurrentGameState().getPatches();

        player1Name.setText(game.getCurrentGameState().getPlayer1().getName());
        player2Name.setText(game.getCurrentGameState().getPlayer2().getName());
        updateMoneyAndScore();
        refreshList();
        showSelectablePatches();
        try {
            loadPatches();
            loadSpecialPatches();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        firstPlayerName = getFirstPlayerName();
        listToClear = new ArrayList<>();
        listToClearGUI = new ArrayList<>();

        listInOrder = new ArrayList<>();
        IOController ioController = mainViewController.getMainController().getIOController();
        List<Patch> patches = ioController.importCSVNotShuffled();
        for(Patch patch : patches) {
            listInOrder.add(new PatchView(patch));
        }
        triggerInitialMove(mainViewController.getMainController().getGame().getCurrentGameState().getPlayer1());
    }

    private void triggerInitialMove(Player startingPlayer) {
        if(startingPlayer.getPlayerType() != PlayerType.HUMAN){
            mainViewController.getMainController().getAIController().doTurn();
        }
    }

    public List<PatchView> getListInOrder(){
        return listInOrder;
    }

    private String getFirstPlayerName(){
        return mainViewController.getMainController().getGameController().getNextPlayer().getName();
    }

    private boolean isFirstPlayer(){
        Player nextPlayer = mainViewController.getMainController().getGameController().getNextPlayer();
        return nextPlayer.getName().equals(firstPlayerName);
    }

    public void updateMoneyAndScore(){
        player1Buttons.setText("Buttons: " + game.getCurrentGameState().getPlayer1().getMoney());
        player2Buttons.setText("Buttons: " + game.getCurrentGameState().getPlayer2().getMoney());
        HighScoreController highScoreController = mainViewController.getMainController().getHighScoreController();
        highScoreController.updateScore(game.getCurrentGameState().getPlayer1());
        highScoreController.updateScore(game.getCurrentGameState().getPlayer2());
        player1Score.setText("Score: " + game.getCurrentGameState().getPlayer1().getScore().getValue());
        player2Score.setText("Score: " + game.getCurrentGameState().getPlayer2().getScore().getValue());
    }

    public void RefreshTheBoard(){
        pane.getChildren().removeAll(listToClear);
        pane.getChildren().removeAll(listToClearGUI);
        listToClear = new ArrayList<>();
        RefreshPlayer(1);
        RefreshPlayer(2);

        Player player1 = mainViewController.getMainController().getGame().getCurrentGameState().getPlayer1();
        Player player2 = mainViewController.getMainController().getGame().getCurrentGameState().getPlayer2();
        if(player1.getBoardPosition() == 53 && player2.getBoardPosition() == 53)
            mainViewController.getGameSummaryViewController().showScene();
    }

    public void RefreshPlayer(int player){
        int counter = 0;
        int currentGameStateIndex = game.getCurrentGameStateIndex();
        System.out.println("currentGameStateIndex: " + currentGameStateIndex);

        for(GameState gameState : game.getGameStatesList()){
            if(counter > currentGameStateIndex)
                break;

            counter++;
            Tuple<Integer, Matrix> t1 = gameState.getPlayer1().getQuiltBoard().getPlacedPatch();
            Tuple<Boolean, Integer> t2 = gameState.getPlayer1().getQuiltBoard().getPlacedPatchOrientation();

            if(player ==2){
                t1 = gameState.getPlayer2().getQuiltBoard().getPlacedPatch();
                t2 = gameState.getPlayer2().getQuiltBoard().getPlacedPatchOrientation();
            }
            try{
                int id = t1.getFirst();
                Matrix placement = t1.getSecond();
                int[][] m = placement.getIntMatrix();
                int[] arr = getStartPos(m);
                boolean flipped = t2.getFirst();
                int rotation = t2.getSecond();

                PatchView patch = listInOrder.get(id-1);
                patch.setFlipped(false);
                patch.setRotation(0);
                pane.getChildren().add(patch);
                listToClear.add(patch);
                for(int i = 0 ; i < rotation/90; i++){
                    patch.rotate();
                }
                if(flipped)
                    patch.flip();
                if(player == 1){
                    patch.setX(60 + (arr[1]) * 30);
                }else{
                    patch.setX(60+ (arr[1]) * 30 + 890);
                }
                patch.setY(60 + (arr[0]) * 30);


                if(patch.isNoNicePatch() && (patch.getRotation() == 90 || patch.getRotation() == 270)){
                    patch.setX(patch.getX() - 15);
                    patch.setY(patch.getY() + 15);
                } else if((patch.getWidth()-patch.getHeight()) == 4 && (patch.getRotation() == 90 || patch.getRotation() == 270)){
                    patch.setX(patch.getX() - 60);
                    patch.setY(patch.getY() + 60);
                }else if((patch.getWidth()-patch.getHeight()) == 2 && (patch.getRotation() == 90 || patch.getRotation() == 270)){
                    patch.setX(patch.getX() - 30);
                    patch.setY(patch.getY() + 30);
                }
            }catch(Exception e){
                System.out.print("");
            }
        }
    }

    public int[] getStartPos(int[][] matrix){
        int[] arr = new int[2];
        arr[1] = 9;
        boolean heightSet = false;
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                if(matrix[i][j] != 0 && !heightSet){
                    arr[0] = i;
                    heightSet = true;
                }
                if(matrix[i][j] != 0 && arr[1] > j){
                    arr[1] = j;
                }
            }
        }
        return arr;
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

    public void loadPatches() throws FileNotFoundException {
        for(Patch patch : patches)
        {
            String path = PatchMap.getInstance().getImagePath(patch);
            imageView = new ImageView(new Image(new FileInputStream("src/" + path)));

            patchListView.getItems().add(new Tuple<>(imageView, patch));
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

    public void loadSpecialPatches(){
        for(int i = 0; i < 5; i++){
            PatchView patchView = specialPatchesOnBoard.get(i);
            if(i == 0){
                patchView.setFitHeight(25);
                patchView.setFitWidth(25);
                patchView.setX(490);
                patchView.setY(290);
                pane.getChildren().add(patchView);
            }
            if(i == 1){
                patchView.setFitHeight(25);
                patchView.setFitWidth(25);
                patchView.setX(562);
                patchView.setY(115);
                pane.getChildren().add(patchView);
            }
            if(i == 2){
                patchView.setFitHeight(25);
                patchView.setFitWidth(25);
                patchView.setX(703);
                patchView.setY(220);
                pane.getChildren().add(patchView);
            }
            if(i == 3){
                patchView.setFitHeight(25);
                patchView.setFitWidth(25);
                patchView.setX(579);
                patchView.setY(151);
                pane.getChildren().add(patchView);
            }
            if(i == 4){
                patchView.setFitHeight(25);
                patchView.setFitWidth(25);
                patchView.setX(597);
                patchView.setY(255);
                pane.getChildren().add(patchView);
            }
        }
    }



    public void PaneDragged() {
    }

    @Override
    public void triggerPlayerTurn() {
        refreshList();
        updateMoneyAndScore();
        RefreshTheBoard();
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
        specialPatches.get(specialPatchIndex).setFitHeight(33);
        specialPatches.get(specialPatchIndex).setFitWidth(33);
        pane.getChildren().remove(specialPatchesOnBoard.get(specialPatchIndex));
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
        updateMoneyAndScore();
        RefreshTheBoard();
    }

    @Override
    public void moveToken(String name, int time) {
        if(name.equals(player1Name.getText())){
            timeToken1.moveToken(time);
        }else{
            timeToken2.moveToken(time);
        }

    }

    @Override
    public void updateLog(String log) {
        logList.getItems().add(log);
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
        UndoRedoController undoRedoController = mainViewController.getMainController().getUndoRedoController();
        undoRedoController.undo();
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
        UndoRedoController undoRedoController = mainViewController.getMainController().getUndoRedoController();
        undoRedoController.redo();
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
        listToClearGUI.add(patchView);
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
        listToClearGUI.add(patchView);
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
        listToClearGUI.add(patchView);
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
        }else if(keyEvent.getCode() == KeyCode.U){ //just to test the movement of the time token on the time board
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
            GameController gameController = mainViewController.getMainController().getGameController();
            boolean placed = gameController.place1x1Patch(activePatchView.getPosX() -2, activePatchView.getPosY()-2, currentPlayer);
            if(placed){
                isPlaced = true;
                refreshList();
                System.out.println("1x1 Patch placed");
            }else{
                System.out.println("there is already a patch");
            }
        }else if(keyEvent.getCode() == KeyCode.K){
            RefreshTheBoard();
        }else if(keyEvent.getCode() == KeyCode.O){
            mainViewController.getGameSummaryViewController().showScene();
        }
    }
}
