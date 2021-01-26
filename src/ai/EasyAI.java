package ai;

import model.*;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class EasyAI extends AI {

    @Override
    public GameState calculateTurn(GameState actualState,Player movingPlayer) {
        List<GameState> resultContainer = getOptions(actualState, movingPlayer);
        return resultContainer.get(new Random().nextInt(resultContainer.size()));
    }

    private LinkedList<GameState> getOptions(GameState actual, Player movingPlayer){
        LinkedList<GameState> result = new LinkedList<>();
        result.add(AIUtil.generateAdvance(actual, movingPlayer).getFirst());

        final Player BEHIND = actual.getPlayer1().equals(movingPlayer)? actual.getPlayer1(): actual.getPlayer2();
        final int MONEY_BEHIND = BEHIND.getMoney();

        List<Patch> patches = AIUtil.getNextPatches(actual);

        patches.stream()
                .filter(patch -> patch.getButtonsCost()<=MONEY_BEHIND)
                .flatMap(patch -> generatePatchLocations(patch, BEHIND.getQuiltBoard()).stream())
                .map(tuple -> {
                    GameState temp = actual.copy();
                    temp.getPatches().remove(tuple.getFirst());
                    Player next = temp.getPlayer1().equals(movingPlayer)? temp.getPlayer1(): temp.getPlayer2();
                    next.setQuiltBoard(tuple.getSecond());
                    next.addMoney(-tuple.getFirst().getButtonsCost());
                    next.setBoardPosition(next.getBoardPosition()+tuple.getFirst().getTime());
                    temp.setLogEntry("Bought patch "+tuple.getFirst().getPatchID()+" and laid it onto the board");
                    return temp;
                })
                .forEach(result::add);

        return result;
    }

    private LinkedHashSet<Tuple<Patch,QuiltBoard>> generatePatchLocations(Patch patch, QuiltBoard quiltBoard) {
        LinkedHashSet<Tuple<Patch,QuiltBoard>> result = new LinkedHashSet<>();
        AIUtil.generateAllPossiblePatches(patch)
                .stream()
                .filter(shape -> shape.getFirst().disjunctive(quiltBoard.getPatchBoard()))
                .map(shape -> {QuiltBoard temp = quiltBoard.copy();
                                temp.addPatch(patch,shape.getFirst(), shape.getSecond().getFirst(), shape.getSecond().getSecond());
                                return new Tuple<>(patch,temp);})
                .forEach(result::add);
        return result;
    }
}
