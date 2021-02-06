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
				.map(this::function)
				.max(Comparator.comparingDouble(Tuple::getFirst))
				.orElse(new Tuple<>(2.0, null))
				.getSecond();
	}

	private Tuple<Double,GameState> function(final GameState gameState){
		Player player2 = gameState.getPlayer2();
		List<Patch> patches = gameState.getPatches();
		double value = 0;
		value = ((double) (calculatePatchValue(patches.get(0), player2)) + evaluateBoard(player2.getQuiltBoard()));
		return new Tuple<>(value,gameState);
 	}

	/**
	 * Evaluates a QuiltBoard and gives it a value, the higher, the better
	 * @param board Quiltboard which should be evaluated
	 * @param filledSpots an Integer on how many places on the board are already filled
	 * @return a double value stating the value of this board
	 */
	private double evaluateBoard(QuiltBoard board, int filledSpots) {
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
