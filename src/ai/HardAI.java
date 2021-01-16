package ai;

import model.*;

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
        Matrix boardMatrix = new Matrix(actualBoard.getPatchBoard());
        Matrix patchMatrix = new Matrix(patch.getShape());
        int filledSpots = boardMatrix.amountCells()-boardMatrix.count(0) + patchMatrix.amountCells()-patchMatrix.count(0);
        return generateAllPossiblePatches(patch)
                .parallelStream()                                                                                   //Generate Patches and parallelize
                .filter(patchPosition -> patchPosition.disjunctive(boardMatrix))                                    //Filter all places which are not valid
                .map(place -> { QuiltBoard copy = actualBoard.clone();
                                //copy.addPatch(patch);
                                return new Tuple<>(copy, evaluateBoard(copy, filledSpots));})                       //map the valid places onto the quiltboard and evaluate the happiness
                .max(Comparator.comparingDouble(Tuple::getSecond))                                                  //Search maximum of happiness eg. best placement
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
        for (int col = 0; col < places[0].length; col++) {    //Subtract one for each side which is covered with an patch
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
        return result;
    }

    /**
     * Generates all possible locations on which a patch could be laid, including mirroring and rotating
     * @param patch patch for which all locations are searched
     * @return a LinkedHashSet containing all possible positions
     */
    private LinkedHashSet<Matrix> generateAllPossiblePatches(Patch patch){
        Matrix shape = new Matrix(patch.getShape());
        LinkedHashSet<Matrix> result = new LinkedHashSet<>();
        for(int side =0; side<2; side++){
            for (int degree = 0; degree < 4; degree++) {
                Matrix trimmed = shape.trim();
                for (int rows = 0; rows <= 9- trimmed.getRows(); rows++) {
                    for (int cols = 0; cols <= 9-trimmed.getColumns(); cols++) {
                        Matrix possiblePlace = new Matrix(9,9);
                        possiblePlace.insert( trimmed, rows, cols);
                        result.add(possiblePlace);
                    }
                }
                shape.rotate();
            }
            shape.flip();
        }
        return result;
    }
}
