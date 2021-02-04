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
        final long actualTime = System.currentTimeMillis();
        final double actualTurn = evaluateBoard(movingPlayer.getQuiltBoard());
        final Player otherPlayer = movingPlayer.lightEquals(actualState.getPlayer1()) ? actualState.getPlayer2() : actualState.getPlayer1();
        final boolean modeEasy = movingPlayer.getQuiltBoard().getPatchBoard().count(0)>40;
        final long START_TIME = System.currentTimeMillis(); // Time measurement// When enough spaces are empty, we care for placement later
        final MinMaxTree<Tuple<GameState, Player>> tree = new MinMaxTree<>(new Tuple<>(actualState, movingPlayer), true); //Let us build a tree

        // The function to build layers into the tree
        final Function<Tuple<GameState, Player>, HashSet<MinMaxTree<Tuple<GameState, Player>>>> createFunction = state -> {
            if(state.getSecond().getBoardPosition()==54){
                var result = new HashSet<MinMaxTree<Tuple<GameState, Player>>>();
                result.add(new MinMaxTree<>(state, false));
                return result;
            }
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
                        if(modeEasy) {
                            moving.getQuiltBoard().getPatches().add(patch); //add patch to list, not to board
                            moving.setBoardPosition(Math.min(moving.getBoardPosition() + patch.getTime(), 54)); //change board position
                            moving.addMoney(-patch.getButtonsCost());
                            Score score = moving.getScore(); //edit score
                            score.setValue(score.getValue() + AIUtil.calculatePatchValue(patch, moving));
                            Player next = moving.getBoardPosition() > other.getBoardPosition() ? other : moving; //get next moving player
                            LinkedHashSet<MinMaxTree<Tuple<GameState, Player>>> temp = new LinkedHashSet<>();
                            temp.add(new MinMaxTree<>(new Tuple<>(copy, next), other.lightEquals(movingPlayer)));
                            return temp.stream();
                        }
                        else{
                            return AIUtil.generateAllPossiblePatches(patch).parallelStream()
                                    .filter(placement -> moving.getQuiltBoard().getPatchBoard().disjunctive(placement.getFirst()))
                                    .map(placement -> {
                                        GameState copyState = copy.copy();
                                        Player copyMove = copyState.getPlayer1().lightEquals(moving)? copyState.getPlayer1() : copyState.getPlayer2();
                                        copyMove.getQuiltBoard().addPatch(patch, placement.getFirst(), placement.getSecond().getFirst(), placement.getSecond().getSecond());
                                        copyMove.setBoardPosition(Math.min(copyMove.getBoardPosition() + patch.getTime(), 54)); //change board position
                                        copyMove.addMoney(-patch.getButtonsCost());
                                        Score score = copyMove.getScore(); //edit score
                                        score.setValue(score.getValue() + AIUtil.calculatePatchValue(patch, moving));
                                        Player next = copyMove.getBoardPosition() > other.getBoardPosition() ? other : copyMove; //get next moving player
                                        return new MinMaxTree<>(new Tuple<>(copyState, next), other.lightEquals(copyMove));
                                    })
                                    .map(child -> new Tuple<>(evaluateBoard(child.getNodeContent().getSecond().getQuiltBoard()), child))
                                    .sequential()
                                    .sorted((o1, o2) -> (int) Math.signum(o1.getFirst()-o2.getFirst()))
                                    .limit(3)
                                    .map(Tuple::getSecond);
                        }
                    })
                    .collect(Collectors.toCollection(HashSet::new));
            set.add(new MinMaxTree<>(AIUtil.generateAdvance(state.getFirst().copy(), state.getSecond().copy()), state.getSecond().lightEquals(movingPlayer))); //get advance option
            return set;
        };
        System.out.println("Start building tree. Preparation ended after "+(System.currentTimeMillis()-actualTime));
        //Actually building the tree
        Runnable buildATree = () -> {
            for (int i = 0; i < 10; i++) { //For when there is time, build additional layer
                try {
                    if (i < 2) tree.createOnLevel(createFunction, i);
                    else tree.createOnLevelAndDelete(createFunction, i);
                }
                catch (InterruptedException e){
                    System.out.println("Whoopsie, I got interrupted after "+(System.currentTimeMillis()-actualTime));
                    return;
                }
                System.out.println("Built level "+i+" which took "+(System.currentTimeMillis()-actualTime));
            }
        };
        Thread treeBuild = new Thread(new ThreadGroup("TreeBuild"),buildATree,"treeBuild");
        treeBuild.start();
        try {
            treeBuild.join(9500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        treeBuild.interrupt();

        System.out.println("Ended building tree. Building ended after "+(System.currentTimeMillis()-actualTime));
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
            if(modeEasy)
                return (double) (thisTurnPlayer.getScore().getValue() - otherTurnPlayer.getScore().getValue());
            else
                return thisTurnPlayer.getScore().getValue()*sigmoid(evaluateBoard(thisTurnPlayer.getQuiltBoard())) - otherTurnPlayer.getScore().getValue()*sigmoid(evaluateBoard(thisTurnPlayer.getQuiltBoard()));
        }); //get max or base
        System.out.println("Ended evaluating tree. Evaluating ended after "+(System.currentTimeMillis()-actualTime));
        bestOption.getSecond().getQuiltBoard().getPatchBoard().print();
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
                System.out.println("Have to place a 1x1-Patch. I needed up to here "+(System.currentTimeMillis()-actualTime)+" milliseconds.");
                Matrix shape = new Matrix(3,5);
                shape.insert(new Matrix(new int[][]{{1}}), 0, 0);
                Patch singlePatch = new Patch(Integer.MAX_VALUE, 0, 0, shape, 0);
                movedPlayer.setQuiltBoard(placePatch(movedPlayer.getQuiltBoard(), singlePatch).getFirst());
                bestState.getTimeBoard()[i].removePatch();
                bestState.setLogEntry(bestState.getLogEntry()+"\nGot a special patch and placed it happily.");
            }
        }
        if(bestState.getLogEntry()==null) bestState.setLogEntry("ERR: LOG_ENTRY_NOT_DEFINED");
        System.out.println("This took me "+(System.currentTimeMillis()-actualTime)+" milliseconds to calculate. Im proud.");
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
            double result = filledSpots*2;
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

    private double sigmoid(double number){
        return 1/(1+Math.pow(Math.E, (-1*number)));
    }


}
