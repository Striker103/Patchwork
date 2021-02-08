package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Matrix;
import model.Patch;
import view.control.MainViewController;

import java.net.URISyntaxException;

public class PatchView extends ImageView {

    public final int id;
    private boolean noNicePatch;
    private boolean flipped;
    private int rotation;
    private int posX = 5;
    private int posY = 5;
    private int delta = 0;
    private final int height;
    private final int width;
    private static final int OFFSET_X = 0;
    private static final int OFFSET_Y = 0;
    private static final int STEPPING = 30;
    private final Patch patch;
    private Matrix matrix;
    private boolean firstPlayer;
    private boolean playerVsPlayer;
    private MainViewController mainViewController;

    /**
     * Constructor for a new patch. Loads it, sets high and with and noNicePatch
     *
     * @param p the patch
     */
    public PatchView(Patch p, boolean b){
        String path;
        if(p.getPatchID() >= 999){
            path = "/view/images/Patches/SpecialPatch.png";
        }else{
            path = PatchMap.getInstance().getImagePath(p);
        }
        try {
            this.setImage(new Image(this.getClass().getResource(path).toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Matrix shape = p.getShape();
        Matrix trim = shape.trim();

        patch = p;
        id = p.getPatchID();
        this.height = trim.getRows();
        this.width = trim.getColumns();
        this.setFitHeight(height * 30);
        this.setFitWidth(width * 30);
        if((height + width) % 2 != 0)
            noNicePatch = true;
        flipped = false;
        rotation = 0;
        playerVsPlayer = b;


        Matrix tmp = new Matrix (width, width);
        int dif = width - height;
        tmp.insert(trim, dif/2 , 0);
        matrix = tmp;
    }

    public Patch getPatch() {
        return patch;
    }

    public int getRotation() {
        return rotation;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public boolean getFlipped() {
        return flipped;
    }

    public void setFirstPlayer(boolean b){
        firstPlayer = b;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getHeight() {
        return height;
    }

    public void setFlipped(boolean b) {
        this.flipped = b;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getWidth() {
        return width;
    }

    public boolean isNoNicePatch() {
        return noNicePatch;
    }

    public boolean isFlipped() {
        return flipped;
    }

    /**
     * flips the Patch and the matrix
     */
    public void flip(){
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
    public void rotate(int side){
        rotation += 90;
        rotation %= 360;
        this.setRotate(rotation);
        if(noNicePatch){
            delta = rotation % 180 != 0 ? STEPPING / 2 : 0;
        }
        moveX(side);
        moveY();
        rotate90();
    }

    public void moveUp(){
        posY--;
        moveY();
    }
    public void moveDown(){
        posY++;
        moveY();
    }
    public void moveLeft(int side){
        posX--;
        moveX(side);
    }
    public void moveRight(int side){
        posX++;
        moveX(side);
    }
    private void moveX(int side){
        int otherBoard = 0;
        //1x1 in player vs player
        if(height == 1 && width == 1 && playerVsPlayer && side == 1){
            otherBoard = 890;
        }
        int extraY = 0;
        //patch in player vs player
        if(side == 2 && !(height == 1 && width == 1) && playerVsPlayer){
            extraY = 890;
        }
        //1x1 in player vs AI
        if(height == 1 && width == 1 && !playerVsPlayer && side == 2){
            otherBoard = 890;
        }
        //patch in player vs AI
        if(!(height == 1 && width == 1) && !playerVsPlayer && side == 2){
            extraY = 890;
        }

        this.setX(OFFSET_X + posX * STEPPING + delta + extraY + otherBoard);

    }
    private void moveY(){
        this.setY(OFFSET_Y + posY * STEPPING+ delta);
    }

    private void rotate90(){
        matrix = matrix.rotate();
    }

    /**
     * checks the respective edge to see whether the patch already touches it
     *
     * @param c character
     * @return true if the move is ok
     */
    public boolean moveIsLegit(char c){
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
    public boolean rotationIsLegit(){
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

    /**
     * places the Patch matrix in a 9x9 matrix and adjusts placement
     *
     * @return the matrix which should represent the board you see on the screen
     */
    public Matrix readyToGo(){
        Matrix matrix = new Matrix(9 , 9);
        Matrix placing = this.matrix;
        Matrix trim = placing.trim();


        if(noNicePatch && (rotation == 90 || rotation == 270) && !(width == 4 && height == 1)){
            matrix.insert(trim, posY -2, (posX - 1));
        }else if((width-height) == 4 && (rotation == 90 || rotation == 270)){
            matrix.insert(trim, posY - 4, posX);
        }else if((width-height) == 2 && (rotation == 90 || rotation == 270)){
            matrix.insert(trim, posY - 3, posX - 1);
        }else if((width == 4 && height == 1) && (rotation == 90 || rotation == 270)){
            matrix.insert(trim, posY - 3, posX);
        }else{
            matrix.insert(trim, posY -2, posX -2);
        }
        return matrix;
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

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}
