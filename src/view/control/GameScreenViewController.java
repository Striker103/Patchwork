package view.control;

import controller.*;
import javafx.event.ActionEvent;
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
import view.aui.ErrorAUI;
import view.aui.LogAUI;
import view.aui.TurnAUI;


public class GameScreenViewController implements TurnAUI , LogAUI {

    private ErrorAUI errorAUI;

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
    private int index = 0;
    private boolean playerVsPlayer;
    private boolean isPlayerTurn;

    @FXML
    private Button rightButton;

    @FXML
    private Button leftButton;

    @FXML
    private Button upButton;

    @FXML
    private Button downButton;

    @FXML
    private Button rotateButton;

    @FXML
    private Button flipButton;

    @FXML
    private Button confirmButton;

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
                    loadButtons();

                }
            }

        });
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void initGame() {
        game = mainViewController.getMainController().getGame();
        int[][] arr = new int[3][5];
        arr[0][0] = 1;
        Matrix shape = new Matrix(arr);
        if(game.getCurrentGameState().getPlayer1().getPlayerType() == PlayerType.HUMAN && game.getCurrentGameState().getPlayer2().getPlayerType() == PlayerType.HUMAN)
            playerVsPlayer = true;
        specialPatchesOnBoard = new ArrayList<>();
        specialPatches = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Patch patch = new Patch(999 + i, 0, 0, shape, 0);
            specialPatches.add(new PatchView(patch, playerVsPlayer));
            specialPatchesOnBoard.add(new PatchView(patch, playerVsPlayer));
        }

        loadTimeBoard();

        patches = game.getCurrentGameState().getPatches();

        player1Name.setText(game.getCurrentGameState().getPlayer1().getName());
        player2Name.setText(game.getCurrentGameState().getPlayer2().getName());
        updateMoneyAndScore();
        refreshList();
        updateIndex();
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
            listInOrder.add(new PatchView(patch, playerVsPlayer));
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

    public void moveTimeTokens(){
        int boardPosP1 = game.getCurrentGameState().getPlayer1().getBoardPosition();
        int boardPosP2 = game.getCurrentGameState().getPlayer2().getBoardPosition();
        moveToken(game.getCurrentGameState().getPlayer1().getName(), boardPosP1);
        moveToken(game.getCurrentGameState().getPlayer2().getName(), boardPosP2);
    }

    public void refreshBoardPatches(){
        for(int i = 0; i < index; i++){
            pane.getChildren().remove(specialPatchesOnBoard.get(i));
        }
    }

    public void refreshTheBoard(){
        pane.getChildren().clear();
        updateIndex();
        addSpecialPatches();
        loadTimeBoard();
        loadSpecialPatches();
        moveTimeTokens();
        //pane.getChildren().removeAll(listToClear);
        //pane.getChildren().removeAll(listToClearGUI);
        listToClear = new ArrayList<>();
        refreshPlayer(1);
        refreshPlayer(2);
        refreshBoardPatches();
        if(!isPlaced)
            placeSpecialTile();

        Player player1 = mainViewController.getMainController().getGame().getCurrentGameState().getPlayer1();
        Player player2 = mainViewController.getMainController().getGame().getCurrentGameState().getPlayer2();
        if(player1.getBoardPosition() == 53 && player2.getBoardPosition() == 53)
            mainViewController.getGameSummaryViewController().showScene();
    }

    public void refreshPlayer(int player){
        int counter = 0;
        int currentGameStateIndex = game.getCurrentGameStateIndex();
        //System.out.println("currentGameStateIndex: " + currentGameStateIndex);

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
                if(matrix[i][j] != 0 && matrix[i][j] != Integer.MAX_VALUE && !heightSet){
                    arr[0] = i;
                    heightSet = true;
                }
                if(matrix[i][j] != 0 && matrix[i][j] != Integer.MAX_VALUE && arr[1] > j){
                    arr[1] = j;
                }
            }
        }
        return arr;
    }

    public void addSpecialPatches(){
        int index = 0;
        Matrix p1Board = game.getCurrentGameState().getPlayer1().getQuiltBoard().getPatchBoard();
        Matrix p2Board = game.getCurrentGameState().getPlayer2().getQuiltBoard().getPatchBoard();
        int[][] p1IntBoard = p1Board.getIntMatrix();
        int[][] p2IntBoard = p2Board.getIntMatrix();

        for(int i = 0; i < p1IntBoard.length; i++){
            for(int j = 0; j < p1IntBoard[i].length; j++){
                if(p1IntBoard[i][j] == Integer.MAX_VALUE ){
                    PatchView patch = specialPatches.get(index);
                    pane.getChildren().add(patch);
                    patch.setX(60 + j * 30);
                    patch.setY(60 + i * 30);
                    index++;
                }
            }
        }
        for(int i = 0; i < p2IntBoard.length; i++){
            for(int j = 0; j < p2IntBoard[i].length; j++){
                if(p2IntBoard[i][j] == Integer.MAX_VALUE ){
                    PatchView patch = specialPatches.get(index);
                    pane.getChildren().add(patch);
                    patch.setX(60 + j * 30 + 890);
                    patch.setY(60 + i * 30);
                    index++;
                }
            }
        }


    }


    public void refreshList()
    {

        patchListView.getItems().clear();

        patchViews = new ArrayList<>();
        patches = game.getCurrentGameState().getPatches();

        for(Patch patch : patches)
        patchViews.add(new PatchView(patch, playerVsPlayer));
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
                GameState gameState = game.getCurrentGameState();
                int boardPos1 = gameState.getPlayer1().getBoardPosition();
                int boardPos2 = gameState.getPlayer2().getBoardPosition();

                int bigger = boardPos1;
                if(boardPos2 > boardPos1)
                    bigger = boardPos2;
                if(bigger >= 50)
                    pane.getChildren().remove(patchView);
            }
        }
    }



    public void PaneDragged() {
    }

    @Override
    public void triggerPlayerTurn() {
        refreshList();
        updateMoneyAndScore();
        refreshTheBoard();
        isPlayerTurn = true;
    }

    public void updateIndex(){
        GameState gameState = game.getCurrentGameState();
        int boardPos1 = gameState.getPlayer1().getBoardPosition();
        int boardPos2 = gameState.getPlayer2().getBoardPosition();

        int bigger = boardPos1;
        if(boardPos2 > boardPos1)
            bigger = boardPos2;

        if(bigger < 20){
            index = 0;
        }else if(bigger < 26){
            index = 1;
        }else if(bigger < 32){
            index = 2;
        }else if(bigger < 44){
            index = 3;
        }else{
            index = 4;
        }
    }

    public void placeSpecialTile(){
        Player currentPlayer = mainViewController.getMainController().getGameController().getNotMovingPlayer();

        updateIndex();
        boolean firstPlayer = isFirstPlayer();
        PatchView patch = specialPatches.get(index);
        try{
            pane.getChildren().add(patch);
        }catch(Exception e){
        }
        if(firstPlayer){
            patch.setX(150 + 890);
            patch.setY(150);
        }else{
            patch.setX(150);
            patch.setY(150);
        }
        if(!playerVsPlayer){
            if(game.getCurrentGameState().getPlayer1().getPlayerType() == PlayerType.HUMAN){
                patch.setX(150);
                patch.setY(150);
            }else{
                patch.setX(150 + 890);
                patch.setY(150);
            }
        }
        activePatchView = patch;
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
        refreshTheBoard();
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

    public void setErrorAUI(ErrorAUI errorAUI) {
        this.errorAUI = errorAUI;
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
                    updateLog("Goal reached!");
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
        passTurn();
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
        choosePatch(0);
    }

    @FXML
    public void onChoose2Action() {
        choosePatch(1);
    }

    @FXML
    public void onChoose3Action() {
        choosePatch(2);
    }

    @FXML
    public void onFlipAction(ActionEvent actionEvent) {
        flipPatch();
    }
    @FXML
    public void onUpAction(ActionEvent actionEvent) {
        movePatchUp();
    }
    @FXML
    public void onRotateAction(ActionEvent actionEvent) {
        rotatePatch();
    }

    @FXML
    public void onLeftAction(ActionEvent actionEvent) {
        movePatchLeft();
    }
    @FXML
    public void onConfirmAction(ActionEvent actionEvent) {
        confirmPatch();
    }

    @FXML
    public void onRightAction(ActionEvent actionEvent) {
        movePatchRight();
    }

    @FXML
    public void onDownAction(ActionEvent actionEvent) {
        movePatchDown();
    }
    private void movePatchUp(){
        if(activePatchView.moveIsLegit('w')){
            activePatchView.setFirstPlayer(isFirstPlayer());
            activePatchView.moveUp();
        }
    }

    private void movePatchRight(){
        if(activePatchView.moveIsLegit('d')){
            activePatchView.setFirstPlayer(isFirstPlayer());
            activePatchView.moveRight();
        }
    }
    private void movePatchLeft(){
        if(activePatchView.moveIsLegit('a')){
            activePatchView.setFirstPlayer(isFirstPlayer());
            activePatchView.moveLeft();
        }

    }
    private void movePatchDown(){
        if(activePatchView.moveIsLegit('s')){
            activePatchView.setFirstPlayer(isFirstPlayer());
            activePatchView.moveDown();
        }
    }
    private void rotatePatch(){
        if(activePatchView.rotationIsLegit()){
            activePatchView.setFirstPlayer(isFirstPlayer());
            activePatchView.rotate();

        }
        else{
            errorAUI.showError("please move away from the corner a little bit");
        }
    }
    private void flipPatch(){
        activePatchView.setFirstPlayer(isFirstPlayer());
        activePatchView.flip();
    }
    private void confirmPatch(){
        if(!isPlaced){
            errorAUI.showError("please place the 1x1 patch first (Key: X)");
            return;
        }
        GameController gameController = mainViewController.getMainController().getGameController();
        gameController.takePatch(activePatchView.getPatch(), activePatchView.readyToGo(), activePatchView.getRotation(), activePatchView.getFlipped());

    }

    private void passTurn(){
        if(!isPlaced){
            errorAUI.showError("please place the 1x1 patch first (Key: X)");
            return;
        }
        removePatches();
        GameController gameController = mainViewController.getMainController().getGameController();
        gameController.advance();
        refreshTheBoard();

    }

    private void choosePatch(int index){
        if(!isPlaced){
            errorAUI.showError("please place the 1x1 patch first (Key: X)");
            return;
        }
        removePatches();
        if(patches.size() <= index){
            return;
        }
        boolean firstPlayer = isFirstPlayer();
        PatchView patchView = patchViews.get(index);
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
          movePatchUp();
        }else if(keyEvent.getCode() == KeyCode.S  || keyEvent.getCode() == KeyCode.NUMPAD2){
              movePatchDown();
        }
        else if(keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.NUMPAD1 ){

            movePatchLeft();
        }
        else if(keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.NUMPAD3){
            movePatchRight();
        }
        else if(keyEvent.getCode() == KeyCode.E || keyEvent.getCode() == KeyCode.NUMPAD6){
            rotatePatch();
        }
        else if(keyEvent.getCode() == KeyCode.Q || keyEvent.getCode() == KeyCode.NUMPAD4) {
            flipPatch();
        }else if(keyEvent.getCode() == KeyCode.R || keyEvent.getCode() == KeyCode.NUMPAD7){
            passTurn();
        }
        else if(keyEvent.getCode() == KeyCode.F || keyEvent.getCode() == KeyCode.NUMPAD9){
            confirmPatch();
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
            boolean placed = false;
            if(game.getCurrentGameState().getPlayer1().getPlayerType() == PlayerType.HUMAN && game.getCurrentGameState().getPlayer2().getPlayerType() == PlayerType.HUMAN){
                Player currentPlayer = mainViewController.getMainController().getGameController().getNotMovingPlayer();
                GameController gameController = mainViewController.getMainController().getGameController();
                placed = gameController.place1x1Patch(activePatchView.getPosX() -2, activePatchView.getPosY()-2, currentPlayer);
            }else{
                Player currentPlayer;
                if(game.getCurrentGameState().getPlayer1().getPlayerType() == PlayerType.HUMAN){
                    currentPlayer = game.getCurrentGameState().getPlayer1();
                }else{
                    currentPlayer = game.getCurrentGameState().getPlayer2();
                }
                GameController gameController = mainViewController.getMainController().getGameController();
                placed = gameController.place1x1Patch(activePatchView.getPosX() -2, activePatchView.getPosY()-2, currentPlayer);
            }
            if(placed){
                isPlaced = true;
                refreshList();
                refreshTheBoard();
                updateLog("1x1 Patch placed");
            }
        }else if(keyEvent.getCode() == KeyCode.K) {
            refreshTheBoard();
        }
    }

    private void loadButtons () {
        try {
            ImageView buttonIcon =new ImageView( new Image(this.getClass().getResource("/view/images/Buttons/Right.png").toURI().toString()));

            buttonIcon.setFitHeight(20);
            buttonIcon.setFitWidth(20);
            rightButton.setGraphic(buttonIcon);

            buttonIcon = new ImageView( new Image(this.getClass().getResource("/view/images/Buttons/Left.png").toURI().toString()));
            buttonIcon.setFitHeight(20);
            buttonIcon.setFitWidth(20);
            leftButton.setGraphic(buttonIcon);

            buttonIcon = new ImageView( new Image(this.getClass().getResource("/view/images/Buttons/Up.png").toURI().toString()));
            buttonIcon.setFitHeight(20);
            buttonIcon.setFitWidth(20);
            upButton.setGraphic(buttonIcon);

            buttonIcon = new ImageView( new Image(this.getClass().getResource("/view/images/Buttons/Down.png").toURI().toString()));
            buttonIcon.setFitHeight(20);
            buttonIcon.setFitWidth(20);
            downButton.setGraphic(buttonIcon);

            buttonIcon = new ImageView( new Image(this.getClass().getResource("/view/images/Buttons/Confirm.png").toURI().toString()));
            buttonIcon.setFitHeight(20);
            buttonIcon.setFitWidth(20);
            confirmButton.setGraphic(buttonIcon);

            buttonIcon = new ImageView( new Image(this.getClass().getResource("/view/images/Buttons/Turn.png").toURI().toString()));
            buttonIcon.setFitHeight(20);
            buttonIcon.setFitWidth(20);
            rotateButton.setGraphic(buttonIcon);

            buttonIcon = new ImageView( new Image(this.getClass().getResource("/view/images/Buttons/Flip.png").toURI().toString()));
            buttonIcon.setFitHeight(20);
            buttonIcon.setFitWidth(20);
            flipButton.setGraphic(buttonIcon);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
