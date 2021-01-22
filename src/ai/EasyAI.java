package ai;

import model.*;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class EasyAI extends AI {

    @Override
    public GameState calculateTurn(GameState actualState) {
        List<GameState> resultContainer = getOptions(actualState);
        return resultContainer.get(new Random().nextInt(resultContainer.size()));
    }

    private LinkedList<GameState> getOptions(GameState actual){
        LinkedList<GameState> result = new LinkedList<>();
        GameState edited = actual.copy();
        Player behind = edited.nextPlayer();
        Player other = edited.getPlayer1().equals(behind)? edited.getPlayer1() : edited.getPlayer2();
        int posBehind = behind.getBoardPosition();
        int posOther = other.getBoardPosition();

        int offset = 0;
        if(posOther != 54) offset = 1;
        behind.setBoardPosition(posOther+offset);
        behind.addMoney(behind.getMoney()+(posOther-posBehind)+offset);
        edited.setLogEntry("Passed and got coins");
        result.add(edited);

        List<Patch> patches = getNextPatches(actual);

        patches
                .stream()
                .filter(patch -> patch.getButtonsCost()<=actual.nextPlayer().getMoney())
                .flatMap(patch -> generatePatchLocations(patch, actual.nextPlayer().getQuiltBoard()).stream())
                .map(tuple -> {
                    GameState temp = actual.copy();
                    temp.getPatches().remove(tuple.getFirst());
                    temp.nextPlayer().setQuiltBoard(tuple.getSecond());
                    temp.nextPlayer().addMoney(temp.nextPlayer().getMoney()-tuple.getFirst().getButtonsCost());
                    temp.nextPlayer().setBoardPosition(temp.nextPlayer().getBoardPosition()+tuple.getFirst().getTime());
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

    private List<Patch> getNextPatches(GameState actual) {
        return actual.getPatches().subList(0,3);
    }
}
