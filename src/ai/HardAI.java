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
     * Calculates the next turn based on the given turn
     * @param actualState the actual state of the game
     * @return the best possible state for the ai to move
     */
    @Override
    public GameState calculateTurn(final GameState actualState,final Player movingPlayer) {
        final long ACTUAL_TIME = System.currentTimeMillis();
        final double actualTurn = evaluateBoard(movingPlayer.getQuiltBoard());
        final Player otherPlayer = movingPlayer.lightEquals(actualState.getPlayer1()) ? actualState.getPlayer2() : actualState.getPlayer1();

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
                            score.setValue(score.getValue() + AIUtil.calculatePatchValue(patch, moving));
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
                                        copyMove.getQuiltBoard().addPatch(patch, placement.getFirst(), placement.getSecond().getFirst(), placement.getSecond().getSecond());
                                        copyMove.setBoardPosition(Math.min(copyMove.getBoardPosition() + patch.getTime(), 54)); //change board position
                                        Score score = copyMove.getScore(); //edit score
                                        score.setValue(score.getValue() + AIUtil.calculatePatchValue(patch, moving));
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
        for (int i = 0; START_TIME + 7000 > System.currentTimeMillis() && i < 8; i++) { //For when there is time, build additional layer
            if (i < 2) tree.createOnLevel(createFunction, i);
            else tree.createOnLevelAndDelete(createFunction, i);
        }

        //Getting the best State of the tree
        var bestOption = tree.calculateMinMaxNode(tuple -> {
            Player thisTurnPlayer, otherTurnPlayer;
            if (tuple.getFirst().getPlayer1().lightEquals(movingPlayer)) { //evaluate players
                thisTurnPlayer = tuple.getFirst().getPlayer1();
                otherTurnPlayer = tuple.getFirst().getPlayer2();
            } else {
                thisTurnPlayer = tuple.getFirst().getPlayer2();
                otherTurnPlayer = tuple.getFirst().getPlayer1();
            }
             return thisTurnPlayer.getScore().getValue() - otherTurnPlayer.getScore().getValue();}); //get max or base
        GameState bestState;
        if (movingPlayer.getQuiltBoard().getPatchBoard().count(0) > 40) {
            if (bestOption.getFirst().equals(actualState)) return null;
            if (bestOption.getFirst().getPatches().equals(actualState.getPatches())) {
                bestState = bestOption.getFirst(); //chosen when advanced
            }
            else {
                //we have to figure out the best placement and the patch used
                Player moved = bestOption.getFirst().getPlayer1().lightEquals(movingPlayer)?bestOption.getFirst().getPlayer1():bestOption.getFirst().getPlayer2();
                Patch used = moved.getQuiltBoard().getPatches().remove(moved.getQuiltBoard().getPatches().size() - 1); //luckily, it is the last patch added
                GameState copy = actualState.copy();
                Player moving;
                if (copy.getPlayer1().lightEquals(movingPlayer)) { //evaluate players
                    moving = copy.getPlayer1();
                } else {
                    moving = copy.getPlayer2();
                }
                copy.tookPatch(used);
                moving.setQuiltBoard(placePatch(moving.getQuiltBoard(), used).getFirst());
                moving.addMoney(-used.getButtonsCost());
                moving.setBoardPosition(Math.min(moving.getBoardPosition() + used.getTime(), 54));
                System.out.println("Score" + moving.getScore().getValue() + "Money" + moving.getMoney());
                copy.setLogEntry("Took patch " + used.getPatchID()+ " for "+used.getButtonsCost()+" coins.");
                bestState = copy;
            }
        }
        else{
            bestState = bestOption.getFirst();
        }
        Player movedPlayer = movingPlayer.lightEquals(bestState.getPlayer1())? bestState.getPlayer1(): bestState.getPlayer2();
        int boardIncome =0;
        for (Patch patch:movedPlayer.getQuiltBoard().getPatches()) {
            boardIncome += patch.getButtonIncome();
        }
        for (int i = movingPlayer.getBoardPosition()+1; i <= movedPlayer.getBoardPosition(); i++) {
            if(bestState.getTimeBoard()[i].hasButton()){
                movedPlayer.addMoney(boardIncome);
                bestState.setLogEntry(bestState.getLogEntry()+"\nGot "+boardIncome+" coins to spend later.");
            }
            if(bestState.getTimeBoard()[i].hasPatch()){
                Matrix shape = new Matrix(3,5);
                shape.insert(new Matrix(new int[][]{{1}}), 0, 0);
                Patch singlePatch = new Patch(Integer.MAX_VALUE, 0, 0, shape, 0);
                movedPlayer.setQuiltBoard(placePatch(movedPlayer.getQuiltBoard(), singlePatch).getFirst());
                bestState.getTimeBoard()[i].removePatch();
                bestState.setLogEntry(bestState.getLogEntry()+"\nGot a special patch and placed it happily.");
            }
        }
        System.out.println(evaluateBoard(movedPlayer.getQuiltBoard()));
        if(bestState.getLogEntry()==null) bestState.setLogEntry("ERR: LOG_ENTRY_NOT_DEFINED");
        System.out.println("This took me "+(System.currentTimeMillis()-ACTUAL_TIME)+" milliseconds to calculate. Im proud.");
        return bestState;
    }

    /**
     * Calculates the best fitting place on the QuiltBoard and the given patch
     *
     * @param actualBoard the Board on which the patch should be applied
     * @param patch the patch which should be placed
     * @return a Tuple of the new QuiltBoard and a Happiness-value higher = better or null, if there is no placement
     */
    public Tuple<QuiltBoard, Double> placePatch(QuiltBoard actualBoard, Patch patch){
        return  AIUtil.generatePatchLocations(patch, actualBoard)
                .stream()
                .map(element -> new Tuple<>(element.getSecond(), evaluateBoard(element.getSecond())))
                .max(Comparator.comparingDouble(Tuple::getSecond))
                .orElse(null);
    }

    /**
     * Evaluates a QuiltBoard and gives it a value, the higher, the better
     * @param board Quiltboard which should be evaluated
     * @param filledSpots an Integer on how many places on the board are already filled
     * @return a double value stating the value of this board
     */
    private Double evaluateBoard(QuiltBoard board, int filledSpots) {
            if (filledSpots >= 81) return Double.MAX_VALUE;
            Matrix places = board.getPatchBoard();
            Matrix framedBoard = new Matrix(11, 11);
            framedBoard.fill(Integer.MAX_VALUE).insert(places, 1, 1);
            int circumferenceOuter = 0; //North and west side of board
            int circOuterExtra = 0; //South and east side of board (is counted again in circumferenceInner)
            for (int col = 0; col < places.getColumns(); col++) {    //Add one for each side which is not covered with an patch
                circumferenceOuter += (places.get(0, col) == 0) ? 1 : 0;
                circumferenceOuter += (places.get(col, 0) == 0) ? 1 : 0;
                circOuterExtra += (places.get(col, places.getColumns()-1) == 0) ? 1 : 0;
                circOuterExtra += (places.get(places.getColumns()-1, col) == 0) ? 1 : 0;
            }
            int circumferenceInner = 0;
            int lonelySpots = 0;
            for (int row = 1; row < framedBoard.getRows() - 1; row++) {
                for (int col = 1; col < framedBoard.getColumns() - 1; col++) {
                    circumferenceInner += (framedBoard.get(row, col) == 0 ^ (framedBoard.get(row + 1, col) == 0)) ? 1 : 0;
                    circumferenceInner += (framedBoard.get(row, col) == 0 ^ (framedBoard.get(row, col + 1) == 0)) ? 1 : 0;

                    if (
                            framedBoard.get(row, col) == 0 ^ framedBoard.get(row - 1, col) == 0 &&
                                    framedBoard.get(row, col) == 0 ^ framedBoard.get(row + 1, col) == 0 &&
                                    framedBoard.get(row, col) == 0 ^ framedBoard.get(row, col - 1) == 0 &&
                                    framedBoard.get(row, col) == 0 ^ framedBoard.get(row, col + 1) == 0
                    )
                        lonelySpots++;
                }
            }
            double result = 0.0 + filledSpots*2;
            int circumferenceTotal = circumferenceInner + circumferenceOuter;
            int deviationFromMain = -(circumferenceTotal - 36); //36 is normal circumference on empty board
            double optimalCircumference = Math.sqrt(filledSpots) * 4; //Circumference when having a square
            double circumferenceExtra = circumferenceTotal - optimalCircumference;
            result += deviationFromMain ^ 5;
            result -= Math.expm1(circumferenceExtra) * 0.5;
            result -= lonelySpots * 25;
            return result;
    }

    private double evaluateBoard(QuiltBoard quiltBoard) {
        return evaluateBoard(quiltBoard, 81-quiltBoard.getPatchBoard().count(0));
    }


}
