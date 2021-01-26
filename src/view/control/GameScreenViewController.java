package view.control;

import controller.GameController;
import controller.GamePreparationController;
import controller.IOController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.*;
import javafx.scene.image.PixelReader;


public class GameScreenViewController {
    
    MainViewController mainViewController;
    
    private Scene ownScene;

    private PatchView activePatchView;

    private int rotation;

    private TimeToken activeTimeToken;

    private List<PatchView> patchViews;

    @FXML
    private Pane pane;

    @FXML
    private ImageView imageView;

    @FXML
    private GridPane gridPane1;

    @FXML
    private GridPane gridPane2;

    @FXML
    private ListView<ImageView> patchListView;

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void setOwnScene(Scene scene) {
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
        timeBoard.setFitWidth(270);
        timeBoard.setFitHeight(270);
        timeBoard.setX(360);
        timeBoard.setY(90);
        pane.getChildren().add(timeBoard);


        TimeToken timeToken = new TimeToken(1);
        timeToken.setX(437);
        timeToken.setY(115); //115
        timeToken.setFitWidth(15);
        timeToken.setFitHeight(15);
        activeTimeToken = timeToken;
        pane.getChildren().add(activeTimeToken);
    }

    //TODO when a patch which was already clicked is clicked again there are warnings
    public void loadPatches() throws FileNotFoundException {
        patchViews = new ArrayList<>();
        IOController ioController = mainViewController.getMainController().getIOController();
        List<Patch> patches = ioController.importCSVNotShuffled();
        for(int i = 1; i < 34; i++) {
            String path = "src/view/images/Patches/Patch" + i + ".png";
            imageView = new ImageView(new Image(new FileInputStream(path)));

            patchListView.getItems().add(imageView);
            Patch p = patches.get(i-1);
            Matrix shape = p.getShape();
            Matrix trim = shape.trim();
            int height = trim.getRows();
            int width = trim.getColumns();
            imageView.setFitHeight(height * 30);
            imageView.setFitWidth(width * 30);
            patchViews.add(new PatchView(p, i));
        }
        patchListView.getItems().add(activeTimeToken);
    }

    public void PaneDragged(MouseEvent mouseEvent) {
    }

    /**
     * changes position and rotation of the activePatch
     *
     * @param keyEvent the pressed kay
     */
    public void handleKeyPressed(KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.NUMPAD5){
            if(activePatchView.moveIsLegit('w')){
                activePatchView.moveUp();
            }else{
                System.out.println("are you blind?");
            }
        }else if(keyEvent.getCode() == KeyCode.S  || keyEvent.getCode() == KeyCode.NUMPAD2){
            if(activePatchView.moveIsLegit('s')){
                activePatchView.moveDown();
            }else{
                System.out.println("are you blind?");
            }
        }
        else if(keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.NUMPAD1 ){
            if(activePatchView.moveIsLegit('a')){
                activePatchView.moveLeft();
            }else{
                System.out.println("are you blind?");
            }
        }
        else if(keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.NUMPAD3){
            if(activePatchView.moveIsLegit('d')){
                activePatchView.moveRight();
            }else{
                System.out.println("are you blind?");
            }
        }
        else if(keyEvent.getCode() == KeyCode.E || keyEvent.getCode() == KeyCode.NUMPAD6){
            if(activePatchView.rotationIsLegit()){
                activePatchView.rotate();
            }else{
                System.out.println("please move away from the corner a little bit");
            }
        }
        else if(keyEvent.getCode() == KeyCode.Q || keyEvent.getCode() == KeyCode.NUMPAD4) {
            activePatchView.flip();
        }
        else if(keyEvent.getCode() == KeyCode.R || keyEvent.getCode() == KeyCode.NUMPAD0){

            try{
                GameController gameController = mainViewController.getMainController().getGameController();
                GameState gameState = mainViewController.getMainController().getGame().getCurrentGameState();
                gameController.takePatch(gameState.getPatchByID(activePatchView.id), activePatchView.readyToGo(), rotation, activePatchView.flipped);
            }catch(NullPointerException e){
                System.out.println("the patch is not placed yet");
            }
        }else if(keyEvent.getCode() == KeyCode.C){ //only for debugging
            Matrix ready = activePatchView.readyToGo();
            ready.print();
            System.out.println();
        }else if(keyEvent.getCode() == KeyCode.V){ //only for debugging

            activePatchView.matrix.print();
            System.out.println();
            System.out.println("posX: " + activePatchView.posX);
            System.out.println("posY: " + activePatchView.posY);
            System.out.println("height: " + activePatchView.height);
            System.out.println("width: " + activePatchView.width);
            System.out.println("NoNicePatch?: " + activePatchView.noNicePatch);
            System.out.println("flipped? " + activePatchView.flipped);
            System.out.println();
        }else if(keyEvent.getCode() == KeyCode.T){ //just to test the movement of the time token on the time board
            activeTimeToken.moveToken();
            System.out.println("POB:" + activeTimeToken.positionOnBoard + " FP:" + activeTimeToken.firstPosX + "," + activeTimeToken.firstPosY + "  LP: " + activeTimeToken.lastPosX + "," + activeTimeToken.lastPosY +"  CP:"+ activeTimeToken.currentPositionX + "," + activeTimeToken.currentPositionY);
        }
    }


    /**
     * in id the number of the patch is stored. noNicePatch is true if the patch does not rotate properly
     */
    private class PatchView extends ImageView {
        private final int id;
        private boolean noNicePatch;
        private boolean flipped;
        private int rotation;
        private int posX = 3;
        private int posY = 3;
        private int delta = 0;
        private final int height;
        private final int width;
        private static final int OFFSET_X = 0;
        private static final int OFFSET_Y = 0;
        private static final int STEPPING = 30;
        Matrix matrix;


        /**
         * Constructor for a new patch. Loads it, sets high and with and noNicePatch
         *
         * @param i number of Patch
         */
        private PatchView(Patch p, int i){
            try {
                this.setImage(new Image(this.getClass().getResource("/view/images/Patches/Patch" + i + ".png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            Matrix shape = p.getShape();
            Matrix trim = shape.trim();

            id = p.getPatchID();
            this.height = trim.getRows();
            this.width = trim.getColumns();
            this.setFitHeight(height * 30);
            this.setFitWidth(width * 30);
            if((height + width) % 2 != 0)
                noNicePatch = true;
            flipped = false;
            rotation = 0;


            Matrix tmp = new Matrix (width, width);
            int dif = width - height;
            tmp.insert(trim, dif/2 , 0);
            matrix = tmp;
        }

        /**
         * flips the Patch and the matrix
         */
        void flip(){
            if(rotation == 90 || rotation == 270){
                swapColumns(matrix.getIntMatrix());
            }else{
                swapRows(matrix.getIntMatrix());
            }
            flipped = !flipped;
            this.setScaleX(flipped ? -1 : 1);
        }

        /**
         * rotates the Patch and the matrix
         */
        void rotate(){
            rotation += 90;
            rotation %= 360;
            this.setRotate(rotation);
            if(noNicePatch){
                delta = rotation % 180 != 0 ? STEPPING / 2 : 0;
            }
            moveX();
            moveY();
            rotate90();
        }

        void moveUp(){
            posY--;
            moveY();
        }
        void moveDown(){
            posY++;
            moveY();
        }

        /**
         * checks the respective edge to see whether the patch already touches it
         *
         * @param c character
         * @return true if the move is ok
         */
        private boolean moveIsLegit(char c){
            Matrix matrix = readyToGo();
            int[][] arr = matrix.getIntMatrix();
            if(c == 'w'){
                for(int i = 0 ; i < 9 ; i++)
                {
                    if(arr[0][i] == 1)
                        return false;
                }
            }else if(c == 's'){
                for(int i = 0 ; i < 9 ; i++)
                {
                    if(arr[8][i] == 1)
                        return false;
                }
            }else if(c == 'a'){
                for(int i = 0 ; i < 9 ; i++)
                {
                    if(arr[i][0] == 1)
                        return false;
                }
            }else if(c == 'd'){
                for(int i = 0 ; i < 9 ; i++)
                {
                    if(arr[i][8] == 1)
                        return false;
                }
            }
            return true;
        }

        /**
         * checks if the patch can be turned without leaving the Board
         *
         * @return true if the Patch can be turned
         */
        private boolean rotationIsLegit(){
            if(height == width)
                return true;
            Matrix matrix = readyToGo();
            int[][] arr = matrix.getIntMatrix();

            int dif = Math.abs(width - height);

            return checkEdges(dif, arr);
        }

        private boolean checkEdges(int i, int[][] arr){
            for(int j = 0 ; j < i && j < 2; j++)
            {
                if(rotation == 90 || rotation == 270)
                {
                    for(int k = 0 ; k < 9 ; k++){
                        if(arr[k][j] == 1 || arr[k][8-j] == 1)
                            return false;
                    }
                }else{
                    for(int k = 0 ; k < 9 ; k++){
                        if(arr[j][k] == 1 || arr[8-j][k] == 1)
                            return false;
                    }
                }
            }
            return true;
        }
        void moveLeft(){
            posX--;
            moveX();
        }
        void moveRight(){
            posX++;
            moveX();
        }

        /**
         * places the Patch matrix in a 9x9 matrix and adjusts placement
         *
         * @return the matrix which should represent the board you see on the screen
         */
        private Matrix readyToGo(){
            Matrix matrix = new Matrix(9 , 9);
            Matrix placing = this.matrix;
            Matrix trim = placing.trim();

            if(noNicePatch && (rotation == 90 || rotation == 270)){
                matrix.insert(trim, posY, (posX + 1));
            }else if((width-height) == 4 && (rotation == 90 || rotation == 270)){
                matrix.insert(trim, posY - 2, posX + 2);
            }else if((width-height) == 2 && (rotation == 90 || rotation == 270)){
                matrix.insert(trim, posY - 1, posX + 1);
            }else{
                matrix.insert(trim, posY, posX);
            }
            return matrix;
        }

        private void moveX(){
            this.setX(OFFSET_X + posX * STEPPING + delta);

        }
        private void moveY(){
            this.setY(OFFSET_Y + posY * STEPPING+ delta);
        }

        private void rotate90(){
            Matrix rotated = matrix.rotate();
            matrix = rotated;
        }

        /**
         *
         * flips on the x axis
         *
         * @param imgArray the matrix to be flipped
         */
        private void swapRows(int[][] imgArray){
            for (int i = 0; i < imgArray.length; i++) {
                for (int j = 0; j < imgArray[i].length / 2; j++) {
                    int temp = imgArray[i][j];
                    imgArray[i][j] = imgArray[i][imgArray.length - 1 - j];
                    imgArray[i][imgArray.length - 1 -j] = temp;

                    this.matrix = new Matrix(imgArray);
                }
            }
        }

        /**
         * flips on the y axis
         *
         * @param imgArray the matrix to be flipped
         */
        private void swapColumns(int[][] imgArray){
            for (int i = 0; i < imgArray.length / 2; i++) {
                for (int j = 0; j < imgArray[i].length; j++) {
                    int temp = imgArray[i][j];
                    imgArray[i][j] = imgArray[imgArray.length - 1 - i][j];
                    imgArray[imgArray.length - 1 -i][j] = temp;

                    this.matrix = new Matrix(imgArray);
                }
            }
        }

    }

    private class TimeToken extends ImageView {
        private int id;
        private int firstPosX = 0;
        private int firstPosY = 0;
        private int lastPosX = 14;
        private int lastPosY = 14;

        private int positionOnBoard = 0;
        int currentPositionX = 4;
        int currentPositionY = 0;

        private static final int STEPPING = 28;
        private int verticalDirection = 1; // -1 = left, 1 = right
        private int horizontalDirection = 1; // -1 = up, 1 = down


        private TimeToken(int id) {
            try {
                this.setImage(new Image(this.getClass().getResource("/view/images/circle.png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            this.setFitWidth(200);
            this.setFitHeight(200);
            //Player currentPlayer = mainViewController.getMainController().getGame().getCurrentGameState().getPlayer1();
            this.id = id;
        }

        void moveToken(int steps){
            for(int i = 0; i < steps; i++){
                moveToken();
            }
        }
        void moveToken(){
            if(verticalDirection == 1 && currentPositionX +2 <= this.lastPosX){
                this.setX(this.getX() + STEPPING * verticalDirection);
                currentPositionX+=2;
                positionOnBoard++;
                if(this.currentPositionX == this.lastPosX && this.currentPositionY == this.firstPosY) {
                    this.horizontalDirection = 1;
                    this.firstPosY += 2;
                }
            }
            else if(verticalDirection == -1 && currentPositionX -2 >= this.firstPosX) {
                this.setX(this.getX() + STEPPING * verticalDirection);
                currentPositionX -= 2;
                positionOnBoard++;
                if (this.currentPositionX == this.firstPosX && this.currentPositionY == this.lastPosY) {
                    this.horizontalDirection = -1;
                    this.lastPosY -= 2;
                }
            }
            else if(horizontalDirection == 1 && currentPositionY +2 <= this.lastPosY){
                this.setY(this.getY() + STEPPING * horizontalDirection);
                currentPositionY+=2;
                positionOnBoard++;
                if(this.currentPositionX == this.lastPosX && this.currentPositionY == this.lastPosY) {
                    this.verticalDirection = -1;
                    this.lastPosX -= 2;
                }
            }
            else if(horizontalDirection == -1 && currentPositionY -2 >= this.firstPosY){
                this.setY(this.getY() + STEPPING * horizontalDirection);
                currentPositionY-=2;
                positionOnBoard++;
                if(this.currentPositionX == this.firstPosX && this.currentPositionY == this.firstPosY) {
                    this.verticalDirection = 1;
                    this.firstPosX += 2;
                }
            }

        }

    }

    @FXML
    public void onUndoAction(ActionEvent actionEvent) {
    }

    @FXML
    public void onPauseAction(ActionEvent actionEvent) {
    }

    @FXML
    public void onPassAction(ActionEvent actionEvent) {
    }

    @FXML
    public void onRedoAction(ActionEvent actionEvent) {
    }

    @FXML
    public void onHintAction(ActionEvent actionEvent) {
    }

    /**
     * is called when a patch is clicked in the patchListView. it gets the index of the clicked Patch and gets the right Patch out of the patches
     * list. Then it makes this Patch visible and sets its position. At the end active Patch is set and rotation is reset.
     */
    @FXML
    public void onDragDetected(){

        int index = patchListView.getSelectionModel().getSelectedIndex();
        PatchView patchView = patchViews.get(index);
        pane.getChildren().add(patchView);
        patchView.setX(90);
        patchView.setY(90);

        activePatchView = patchView;
        rotation = 0;
    }
}
