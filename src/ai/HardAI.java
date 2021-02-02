package ai;

import model.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The hard AI. Hopefully the best.
 * @author Lukas Kidin
 */
public class HardAI extends AI {
    /**
     * the positions of the buttons on the time board
     */
    private final int[] buttonPositions = {5, 11, 17, 23, 29, 35, 41, 47, 53};
    private final int[] patchPositions = {20, 26, 32, 44, 50};

    /**
     * Calculates the next turn based on the given turn
     * @param actualState the actual state of the game
     * @return the best possible state for the ai to move
     */
    @Override
    public GameState calculateTurn(final GameState actualState,final Player movingPlayer) {

        final double actualTurn = evaluateBoard(movingPlayer.getQuiltBoard());

        final long START_TIME = System.currentTimeMillis(); // Time measurement// When enough spaces are empty, we care for placement later
        final MinMaxTree<Tuple<GameState, Player>> tree = new MinMaxTree<>(new Tuple<>(actualState, movingPlayer), true); //Let us build a tree

        // The function to build layers into the tree
        final Function<Tuple<GameState, Player>, HashSet<MinMaxTree<Tuple<GameState, Player>>>> createFunction = state -> {
            HashSet<MinMaxTree<Tuple<GameState, Player>>> set = Arrays.stream(state.getFirst().getNext3Patches()) //next patch options
                    .filter(patch -> patch.getButtonsCost() <= movingPlayer.getMoney()) //check money
                    .flatMap(patch -> {
                        GameState copy = state.getFirst().copy();
                        copy.tookPatch(patch); // Remove Patch from copy
                        Player moving, other;
                        if (copy.getPlayer1().lightEquals(state.getSecond())) { //evaluate players
                            moving = copy.getPlayer1();
                            other = copy.getPlayer2();
                        } else {
                            moving = copy.getPlayer2();
                            other = copy.getPlayer1();
                        }
                        if(movingPlayer.getQuiltBoard().getPatchBoard().count(0)>40) {
                            moving.getQuiltBoard().getPatches().add(patch); //add patch to list, not to board
                            moving.setBoardPosition(Math.min(moving.getBoardPosition() + patch.getTime(), 54)); //change board position
                            Score score = moving.getScore(); //edit score
                            score.setValue(score.getValue() + calculatePatchValue(patch, moving));
                            Player next = moving.getBoardPosition() > other.getBoardPosition() ? other : moving; //get next moving player
                            LinkedHashSet<MinMaxTree<Tuple<GameState, Player>>> temp = new LinkedHashSet<>();
                            temp.add(new MinMaxTree<>(new Tuple<>(copy, next), other.lightEquals(movingPlayer)));
                            return temp.stream();
                        }
                        else{
                            return AIUtil.generateAllPossiblePatches(patch).stream()
                                    .filter(placement -> moving.getQuiltBoard().getPatchBoard().disjunctive(placement.getFirst()))
                                    .map(placement -> {
                                        Player copyMove = moving.copy();
                                        GameState copyState = copy.copy();
                                        copyMove.getQuiltBoard().addPatch(patch, placement.getFirst(), placement.getSecond().getFirst() *90, placement.getSecond().getSecond());
                                        copyMove.setBoardPosition(Math.min(copyMove.getBoardPosition() + patch.getTime(), 54)); //change board position
                                        Score score = copyMove.getScore(); //edit score
                                        score.setValue(score.getValue() + calculatePatchValue(patch, moving));
                                        Player next = copyMove.getBoardPosition() > other.getBoardPosition() ? other : copyMove; //get next moving player
                                        return new MinMaxTree<>(new Tuple<>(copyState, next), other.lightEquals(copyMove));
                                    })
                                    .map(child -> new Tuple<>(evaluateBoard(child.getNodeContent().getSecond().getQuiltBoard()), child))
                                    .sorted((o1, o2) -> (int) Math.signum(o1.getFirst()-o2.getFirst()))
                                    .limit(3)
                                    .map(Tuple::getSecond);
                        }
                    })
                    .collect(Collectors.toCollection(HashSet::new));
            set.add(new MinMaxTree<>(AIUtil.generateAdvance(state.getFirst().copy(), state.getSecond().copy()), state.getSecond().lightEquals(movingPlayer))); //get advance option
            return set;
        };

        //Actually building the tree
        for (int i = 0; START_TIME + 7000 > System.currentTimeMillis() && i < 7; i++) { //For when there is time, build additional layer
            if (i < 2) tree.createOnLevel(createFunction, i);
            else tree.createOnLevelAndDelete(createFunction, i);
        }

        //Getting the best State of the tree
        var bestOption = tree.calculateMinMaxNode(tuple -> tuple.getFirst().getPlayer1().getScore().getValue() - tuple.getFirst().getPlayer2().getScore().getValue()); //get max or base
        GameState bestState;
        if (movingPlayer.getQuiltBoard().getPatchBoard().count(0) > 40) {
            if (bestOption.getFirst().equals(actualState)) return null;
            if (bestOption.getFirst().getPatches().equals(actualState.getPatches()))
                return bestOption.getFirst(); //chosen when advanced
            //we have to figure out the best placement and the patch used
            Patch used = bestOption.getSecond().getQuiltBoard().getPatches().remove(bestOption.getSecond().getQuiltBoard().getPatches().size() - 1); //luckily, it is the last patch added
            GameState copy = actualState.copy();
            Player moving;
            if (copy.getPlayer1().lightEquals(movingPlayer)) { //evaluate players
                moving = copy.getPlayer1();
            } else {
                moving = copy.getPlayer2();
            }
            moving.setQuiltBoard(placePatch(moving.getQuiltBoard(), used).getFirst());
            copy.setLogEntry("Took patch " + used.getPatchID());
            return copy;
        }

        return bestOption.getFirst();
    }

    /**
     * Calculates the value of the patch
     * @param patch patch which value should be calculated
     * @param next the player for which the patch should be calculated
     * @return the value the patch has
     */
    private int calculatePatchValue(Patch patch, Player next){
        int result = 0;
        for (int pos: buttonPositions) {
            result += next.getBoardPosition()<pos?patch.getButtonIncome():0;
        }
        result -= patch.getButtonsCost();
        result += patch.getShape().count(1)*2;
        return result;
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
                .filter(patchPosition -> patchPosition.getFirst().disjunctive(boardMatrix))                         //Filter all places which are not valid
                .map(place -> { QuiltBoard copy = actualBoard.copy();
                                copy.addPatch(patch, place.getFirst(), place.getSecond().getFirst() * 90, place.getSecond().getSecond());
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

    private double evaluateBoard(QuiltBoard quiltBoard) {
        return evaluateBoard(quiltBoard, 81-quiltBoard.getPatchBoard().count(0));
    }


}
