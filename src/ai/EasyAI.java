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
                .map(tuple -> AIUtil.buyPatch(tuple, actual, movingPlayer))
                .forEach(result::add);
        return result;
    }


}
