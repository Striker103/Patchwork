package ai;

import model.*;

import java.util.*;

public class EasyAI extends AI {

    @Override
    public GameState calculateTurn(GameState actualState,Player movingPlayer) {
        List<GameState> resultContainer = getOptions(actualState, movingPlayer);
        GameState result = resultContainer.get(new Random().nextInt(resultContainer.size()));

        Player thisTurnPlayer, otherTurnPlayer;
        if (result.getPlayer1().lightEquals(movingPlayer)) { //evaluate players
            thisTurnPlayer = result.getPlayer1();
        } else {
            thisTurnPlayer = result.getPlayer2();
        }
        int boardIncome =0;
        for (Patch patch:thisTurnPlayer.getQuiltBoard().getPatches()) {
            boardIncome += patch.getButtonIncome();
        }

        for(int spot = movingPlayer.getBoardPosition()+1; spot<= Math.min(thisTurnPlayer.getBoardPosition(), 53); spot++){
            if(result.getTimeBoard()[spot].hasButton()){
                thisTurnPlayer.addMoney(boardIncome);
                result.setLogEntry(result.getLogEntry()+"\nGot "+boardIncome+" coins to spend later.");
            }
            if(result.getTimeBoard()[spot].hasPatch()){
                thisTurnPlayer.setQuiltBoard(placeRandomPatch(thisTurnPlayer));
                result.getTimeBoard()[spot].removePatch();
                result.setLogEntry(result.getLogEntry()+"\nGot a special patch and placed it happily.");
            }
        }

        return result;
    }

    private QuiltBoard placeRandomPatch(Player hasToPlace) {
        Matrix shape = new Matrix(3,5);
        shape.insert(new Matrix(new int[][]{{1}}), 0, 0);
        Patch singlePatch = new Patch(Integer.MAX_VALUE, 0, 0, shape, 0);
        var places = AIUtil.generatePatchLocations(singlePatch, hasToPlace.getQuiltBoard());
        return places.stream().skip(new Random().nextInt(places.size()-1)).findFirst().orElse(new Tuple<>(singlePatch, hasToPlace.getQuiltBoard())).getSecond();
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
