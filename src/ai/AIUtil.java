package ai;

import model.*;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Class for Utilities addressing the AI
 * @author Lukas Kidin
 */
public final class AIUtil {

    private static final int[] buttonPositions = {5, 11, 17, 23, 29, 35, 41, 47, 53};
    private static final int[] patchPositions = {20, 26, 32, 44, 50};
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
                        result.add(new Tuple<>(possiblePlace, new Tuple<>(degree*90, side != 0)));
                    }
                }
                shape.rotate();
            }
            shape.flip();
        }
        return result;
    }

    /**
     * Generate all Patchlocations for a given patch and a board
     * @param patch patch which should be laid onto board
     * @param quiltBoard the quiltboard which should be laid onto
     * @return set of Tuples containing the quiltboard and the patch in all possible positioning (patch only for reference)
     */
    public static LinkedHashSet<Tuple<Patch,QuiltBoard>> generatePatchLocations(Patch patch, QuiltBoard quiltBoard) {
        LinkedHashSet<Tuple<Patch,QuiltBoard>> result = new LinkedHashSet<>();
        AIUtil.generateAllPossiblePatches(patch)
                .stream()
                .filter(shape -> shape.getFirst().disjunctive(quiltBoard.getPatchBoard()))
                .filter(distinctByKey(Tuple::getFirst))
                .map(shape -> {QuiltBoard temp = quiltBoard.copy();
                    temp.addPatch(patch,shape.getFirst(), shape.getSecond().getFirst(), shape.getSecond().getSecond());
                    return new Tuple<>(patch,temp);})
                .forEach(result::add);
        return result;
    }

    /**
     * Filter to filter by a parameter, not by the complete object
     * @param keyExtractor the parameter for which should be filtered
     * @param <T> Type of Object
     * @return a Predicate for T
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    /**
     * Generates the Gamestate which would follow, if the current player advances and takes coins
     * @param state the actual state
     * @param next the current player
     * @return a tuple containing the resulting state and the player which turn it is in that state (the other)
     */
    public static Tuple<GameState, Player> generateAdvance(GameState state, Player next){
        GameState edited = state.copy();
        final Player BEHIND = edited.getPlayer1().lightEquals(next)? edited.getPlayer1(): edited.getPlayer2();
        final Player OTHER = edited.getPlayer1().lightEquals(BEHIND)? edited.getPlayer2() : edited.getPlayer1();
        int posBehind = BEHIND.getBoardPosition();
        int posOther = OTHER.getBoardPosition();

        int offset = 0;
        if(posOther != 54) offset = 1;
        BEHIND.setBoardPosition(posOther+offset);
        BEHIND.addMoney((posOther-posBehind)+offset);
        edited.setLogEntry(next.getName()+" passed and got "+((posOther-posBehind)+offset)+" coins");
        return new Tuple<>(edited, OTHER);
    }

    /**
     * Creates GameState which will show the stuff, when a patch is already placed but not bought
     * @param tuple tuple containing Bought Patch and Quiltboard, where Patch already lies
     * @param actual the actual state from which the changes should be made
     * @param movingPlayer the Player who buys this patch
     * @return a GameState with all these changes made
     */
    public static GameState buyPatch(Tuple<Patch, QuiltBoard> tuple, GameState actual, Player movingPlayer){
        Patch used = tuple.getFirst();
        GameState temp = actual.copy();
        temp.getPatches().remove(used);
        Player next = temp.getPlayer1().equals(movingPlayer)? temp.getPlayer1(): temp.getPlayer2();
        next.setQuiltBoard(tuple.getSecond());
        next.addMoney(-used.getButtonsCost());
        next.setBoardPosition(Math.min(next.getBoardPosition()+used.getTime(),54));
        temp.setLogEntry("Bought patch "+used.getPatchID()+" for "+used.getButtonsCost()+" coins and laid it onto the board");
        return temp;
    }

    /**
     * Calculates the value of the patch
     * @param patch patch which value should be calculated
     * @param next the player for which the patch should be calculated
     * @return the value the patch has
     */
    public static int calculatePatchValue(Patch patch, Player next){
        int result = 0;
        for (int pos: buttonPositions) {
            result += next.getBoardPosition()<pos?patch.getButtonIncome():0;
        }
        result -= patch.getButtonsCost();
        result += patch.getShape().count(1)*2;
        return result;
    }

    /**
     * get the next patches
     * @param actual actual state
     * @return a sublist containing the next three patches
     * @deprecated use method from gamestate instead
     */
    @Deprecated
    public static List<Patch> getNextPatches(GameState actual) {
        return actual.getPatches().subList(0,3);
    }
}
