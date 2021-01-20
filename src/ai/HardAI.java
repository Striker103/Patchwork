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
        Matrix boardMatrix = actualBoard.getPatchBoard();
        Matrix patchMatrix = patch.getShape();
        int filledSpots = boardMatrix.amountCells()-boardMatrix.count(0) + patchMatrix.amountCells()-patchMatrix.count(0);
        return AIUtil.generateAllPossiblePatches(patch)
                .parallelStream()                                                                                   //Generate Patches and parallelize
                .filter(patchPosition -> patchPosition.getFirst().disjunctive(boardMatrix))                                    //Filter all places which are not valid
                .map(place -> { QuiltBoard copy = actualBoard.copy();
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
        Matrix places = board.getPatchBoard();
        double circumferenceOuter=18; //Value for empty board
        for (int col = 0; col < places.getColumns(); col++) {    //Subtract one for each side which is covered with an patch
            circumferenceOuter-= (places.get(0,col)!=0) ? 1 : 0;
            circumferenceOuter-= (places.get(col,0)!=0) ? 1 : 0;
            circumferenceOuter-= (places.get(8,col)!=0) ? 1 : 0;
            circumferenceOuter-= (places.get(col,8)!=0) ? 1 : 0; // Yes, the edges more than one time, because they also have more than one side
        }
        double circumferenceInner =0;
        for (int row = 0; row < places.getRows(); row++) {
            for (int col = 0; col < places.getColumns() ; col++) {
                circumferenceInner+= (places.get(row,col)!=0 ^ (row+1>=9||(places.get(row+1,col)!=0))) ?1:0;
                circumferenceInner+= (places.get(row,col)!=0 ^ (col+1>=9||(places.get(row,col+1)!=0))) ?1:0;
            }
        }
        double intermediate = 4/(circumferenceInner+circumferenceOuter);
        double result = 1-(circumferenceInner/filledSpots);
        result += intermediate;
        return result;
    }


}
