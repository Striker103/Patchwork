package ai;

import model.GameState;
import model.Patch;
import model.QuiltBoard;
import model.Tuple;

import java.util.*;

/**
 * The hard AI. Hopefully the best.
 * @author Lukas Kidin
 */
public class HardAI extends AI {
    /**
     * Calculates the next turn based on the given turn
     * @param actualState the actual state of the game
     * @return the best possible state for the ai to move
     */
    @Override
    public GameState calculateTurn(GameState actualState) {
        return null;
    }

    /**
     * Calculates the best fitting place on the QuiltBoard and the given patch
     *
     * @param actualBoard the Board on which the patch should be applied
     * @param patch the patch which should be placed
     * @return a Tuple of the new QuiltBoard and a Happiness-value ranging from 0 to 1 (inclusive) higher = better or null, if there is no placement
     */
    public Tuple<QuiltBoard, Double> placePatch(QuiltBoard actualBoard, Patch patch){
        int filledSpots = AIUtil.filledPlaces(actualBoard.getPatchBoard()) + AIUtil.filledPlaces(patch.getShape());
        return generateAllPossiblePatches(patch).parallelStream() //Generate Patches and parallelize
                .filter(patchPosition -> AIUtil.isPossible(actualBoard, patchPosition)) //Filter all places which are not valid
                .map(place -> {QuiltBoard copy = actualBoard.copy();
                    //copy.addPatch(patch);
                    return new Tuple<>(copy, evaluateBoard(copy, filledSpots));}) //map the valid places onto the quiltboard and evaluate the happiness
                .max(Comparator.comparingDouble(Tuple::getSecond)) //Search maximum of happiness eg. best placement
                .orElse(null);
    }

    /**
     * Evaluates a QuiltBoard and gives it a value, the higher, the better
     * @param board Quiltboard which should be evaluated
     * @param filledSpots an Integer on how many places on the board are already filled
     * @return a double value stating the value of this board
     */
    private Double evaluateBoard(QuiltBoard board, int filledSpots) {
        if(filledSpots>=81) return Double.MAX_VALUE;
        int[][] places = board.getPatchBoard();
        double circumferenceOuter=18; //Value for empty board
        for (int col = 0; col < places[0].length; col++) { //Subtract one for each side which is covered with an patch
            circumferenceOuter-= (places[0][col]!=0) ? 1 : 0;
            circumferenceOuter-= (places[col][0]!=0) ? 1 : 0;
            circumferenceOuter-= (places[8][col]!=0) ? 1 : 0;
            circumferenceOuter-= (places[col][8]!=0) ? 1 : 0; // Yes, the edges more than one time, because they also have more than one side
        }
        double circumferenceInner =0;
        for (int row = 0; row < places.length; row++) {
            for (int col = 0; col < places[row].length ; col++) {
                circumferenceInner+= (places[row][col]!=0 ^ (row+1>=9||(places[row+1][col]!=0))) ?1:0;
                circumferenceInner+= (places[row][col]!=0 ^ (col+1>=9||(places[row][col+1]!=0))) ?1:0;
            }
        }
        double intermediate = 4/(circumferenceInner+circumferenceOuter);
        double result = 1-(circumferenceInner/filledSpots);
        result += intermediate;
        return result;
    }

    /**
     * Generates all possible locations on which a patch could be laid, including mirroring and rotating
     * @param patch patch for which all locations are searched
     * @return a LinkedHashSet containing all possible positions
     */
    private LinkedHashSet<boolean[][]> generateAllPossiblePatches(Patch patch){
        boolean[][] shape = patch.getShape();
        LinkedHashSet<boolean[][]> result = new LinkedHashSet<>();
        for(int side =0; side<2; side++){
            for (int degree = 0; degree < 4; degree++) {
                boolean[][] trimmed = trimShape(shape);
                for (int rows = 0; rows <= 9- trimmed.length; rows++) {
                    for (int cols = 0; cols <= 9-trimmed[0].length; cols++) {
                        boolean[][] possiblePlace = new boolean[9][9];
                        AIUtil.insert(possiblePlace, trimmed, rows, cols);
                        result.add(possiblePlace);
                    }
                }
                AIUtil.rotate(shape);
            }
            AIUtil.flip(shape);
        }
        return result;
    }

    /**
     * Trims falsevalued columns and falsevalued rows from an connected component matrix
     * @param shape the matrix to be trimmed. This matrix wont be altered
     * @return a new trimmed matrix.
     */
    private boolean[][] trimShape(boolean[][] shape){
        boolean[] emptyRows = new boolean[shape.length];
        boolean[] emptyColumns = new boolean[shape[0].length];

        //Check for empty rows and columns
        for(int rows=0; rows<shape.length; rows++){
            for(int cols=0; cols<shape[rows].length; cols++){
                if(shape[rows][cols]){emptyRows[rows]=false; break;}
                emptyRows[rows] = true;
            }
        }

        for(int cols=0; cols<shape[0].length; cols++){
            for (boolean[] booleans : shape) {
                if (booleans[cols]) {
                    emptyColumns[cols] = false;
                    break;
                }
                emptyColumns[cols] = true;
            }
        }

        //Because patches are a connected component we can count the false in rows and columns
        int rows = 5-AIUtil.filledPlaces(new boolean[][]{emptyRows});
        int cols = 5-AIUtil.filledPlaces(new boolean[][]{emptyColumns});

        boolean[][] result = new boolean[rows][cols];

        rows=0;
        for(int i=0; i<shape.length; i++){
            if(emptyRows[i]) continue;
            cols=0;
            for(int j=0; j<shape[i].length; j++){
                if(emptyColumns[j]) continue;
                result[rows][cols]=shape[i][j];
                cols++;
            }
            rows++;
        }
        return result;
    }
}
