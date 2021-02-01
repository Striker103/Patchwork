package ai;


import model.*;

import java.util.*;


/**
 * The normal AI
 * @author Abdullah Ourfali
 */
public class NormalAI extends AI {

	/**
	 * the positions of the buttons on the time board
	 */
	private final int[] buttonPositions = {5, 11, 17, 23, 29, 35, 41, 47, 53};

	/**
	 * Calculates the next turn based on the given turn
	 * @param actualState the actual state of the game
	 * @return the best possible state for the ai to move
	 */
	@Override
	public GameState calculateTurn(GameState actualState, Player movingPlayer) {
		List<GameState> resultContainer = getOptions(actualState, movingPlayer);
		return resultContainer.stream()
				.map( GameState ->{ return function( GameState );})
				.max(Comparator.comparingDouble(Tuple::getFirst))
				.orElse(null)
				.getSecond();
	}

	private Tuple<Double,GameState> function( final GameState gameState){
		GameState  gameStateCopy = gameState.copy();
		Player player1 = gameStateCopy.getPlayer1();
		Patch[] patches = gameStateCopy.getNext3Patches();
		double maximum = 0;
		double temp = 0;
		for ( int i=0; i<2; i++ ){
			Patch patch = patches[i];
			temp = ((double) (calculatePatchValue(patch, player1)))/patch.getTime();
				if ( maximum <= temp ){
					maximum = temp;
				}
			}
		return new Tuple<>(maximum,gameState );
 	}

	private LinkedList<GameState> getOptions(GameState actual, Player movingPlayer){
		LinkedList<GameState> result = new LinkedList<>();
		result.add(AIUtil.generateAdvance(actual, movingPlayer).getFirst());
		final Player BEHIND = actual.getPlayer1().equals(movingPlayer)? actual.getPlayer1(): actual.getPlayer2();
		List<Patch> patches = AIUtil.getNextPatches(actual);
		patches.stream()
				.filter( patch -> patch.getButtonsCost() <= BEHIND.getMoney())
				.flatMap( patch -> generatePatchLocations( patch, BEHIND.getQuiltBoard()).stream())
				.map( tuple -> {
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

	private LinkedHashSet<Tuple<Patch, QuiltBoard>> generatePatchLocations(Patch patch, QuiltBoard quiltBoard) {
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

	/**
	 * Calculates the value of the patch
	 * @param patch patch which value should be calculated
	 * @param next the player for which the patch should be calculated
	 * @return the value the patch has
	 */
	private int calculatePatchValue(Patch patch, Player next){
		int result = 0;
		for (int pos: buttonPositions) {
			result += next.getBoardPosition()<pos ? patch.getButtonIncome() : 0;
		}
		result -= patch.getButtonsCost();
		result += patch.getShape().count(1)*2;
		return result;
	}

}
