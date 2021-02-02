package ai;

import model.*;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Class for Utilities addressing the AI
 * @author Lukas Kidin
 */
public final class AIUtil {
    /**
     * Generates all possible locations on which a patch could be laid, including mirroring and rotating
     * @param patch patch for which all locations are searched
     * @return a LinkedHashSet containing all possible positions
     */
    protected static LinkedHashSet<Tuple<Matrix, Tuple<Integer, Boolean>>> generateAllPossiblePatches(Patch patch){
        Matrix shape = new Matrix(5,5);
        shape.insert(patch.getShape().copy(),0,0);
        LinkedHashSet<Tuple<Matrix, Tuple<Integer, Boolean>>> result = new LinkedHashSet<>();
        for(int side =0; side<2; side++){
            for (int degree = 0; degree < 4; degree++) {
                Matrix trimmed = shape.trim();
                for (int rows = 0; rows <= 9- trimmed.getRows(); rows++) {
                    for (int cols = 0; cols <= 9-trimmed.getColumns(); cols++) {
                        Matrix possiblePlace = new Matrix(9,9);
                        possiblePlace.insert( trimmed, rows, cols);
                        result.add(new Tuple<>(possiblePlace, new Tuple<>(degree*90, side == 0)));
                    }
                }
                shape.rotate();
            }
            shape.flip();
        }
        return result;
    }

    public static Tuple<GameState, Player> generateAdvance(GameState state, Player next){
        GameState edited = state.copy();
        final Player BEHIND = edited.getPlayer1().lightEquals(next)? edited.getPlayer1(): edited.getPlayer2();
        final Player OTHER = edited.getPlayer1().lightEquals(BEHIND)? edited.getPlayer2() : edited.getPlayer1();
        int posBehind = BEHIND.getBoardPosition();
        int posOther = OTHER.getBoardPosition();
        final int MONEY_BEHIND = BEHIND.getMoney();

        int offset = 0;
        if(posOther != 54) offset = 1;
        BEHIND.setBoardPosition(posOther+offset);
        BEHIND.addMoney((posOther-posBehind)+offset);
        edited.setLogEntry("Passed and got coins");
        return new Tuple<>(edited, OTHER);
    }

    public static List<Patch> getNextPatches(GameState actual) {
        return actual.getPatches().subList(0,3);
    }
}
