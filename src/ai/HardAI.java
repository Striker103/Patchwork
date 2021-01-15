package ai;

import model.GameState;
import model.Patch;
import model.QuiltBoard;
import model.Tuple;

import java.util.*;

public class HardAI extends AI {

    @Override
    public GameState calculateTurn(GameState actualState) {
        return null;
    }

    /**
     * Calculates the best fitting place on the QuiltBoard and the patch
     *
     * @param actualBoard the Board on which the patch should be applied
     * @param patch the patch which should be placed
     * @return a Tuple of the new QuiltBoard and a Happiness-value ranging from 0 to 1 (inclusive) higher = better
     */
    private Tuple<QuiltBoard, Double> placePatch(QuiltBoard actualBoard, Patch patch){
        return new Tuple<>(actualBoard, 0.0);
    }

    private Collection<boolean[][]> generateAllPossiblePatches(Patch patch){
        Set<boolean[][]> result = new HashSet<>();
        for(int side =0; side<2; side++){
            for (int degree = 0; degree < 4; degree++) {

            }
        }
        return result;
    }

    private boolean[][] trimShape(Patch patch){
        boolean[][] shape = patch.getShape();
        Boolean[] emptyRows = new Boolean[shape.length];
        Boolean[] emptyColumns = new Boolean[shape[0].length];

        //Check for empty rows and columns
        for(int i=0; i<shape.length; i++){
            for(int j=0; j<shape[i].length; j++){
                if(shape[i][j]) break;
                emptyRows[i] = true;
            }
        }

        for(int j=0; j<shape[0].length; j++){
            for(int i=0; i<shape.length; i++){
                if(shape[j][i]) break;
                emptyColumns[j] = true;
            }
        }

        //Because patches are a connected component we can count the false in rows and columns
        int rows = (int) Arrays.stream(emptyRows).filter(bool -> !bool).count();
        int cols = (int) Arrays.stream(emptyColumns).filter(bool -> !bool).count();

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
