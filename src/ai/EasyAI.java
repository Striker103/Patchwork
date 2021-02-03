package ai;

import model.*;

import java.util.*;

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

        List<Patch> patches = Arrays.asList(actual.getNext3Patches().clone());

        patches.stream()
                .filter(patch -> patch.getButtonsCost()<=MONEY_BEHIND)
                .flatMap(patch -> AIUtil.generatePatchLocations(patch, BEHIND.getQuiltBoard()).stream())
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


}
