package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dennis Querndt
 * GameState is an Object which holds all informations regarding the current player move. Every player move has its own GameState
 */
public class GameState{
	/**
	 * checks if the one and only Special Tile is still available
	 */
	private boolean specialTileAvailable;

	/**
	 * protocol for all changes at each move
	 */
	private String logEntry;

	/**
	 * The whole List represents the timeBoard. Each TimeBoardComponent holds the information if the field has a button or a 1x1patch on it
	 */
	private final TimeBoardComponent[] timeBoard;

	/**
	 * The List with all patches which are not taken by players yet
	 */
	private List<Patch> patches;

	/**
	 * first player
	 */
	private final Player player1;

	/**
	 * second player
	 */
	private final Player player2;

	/**
	 * Constructor for a new GameState Object
	 *
	 * @param boardPositions		board position of players
	 * @param playerMoney			money of players
	 * @param playerNames 			names of players
	 * @param patches 				list of patches
	 * @param specialTileAvailable	true if the bonus tile is still available
	 *
	 * @throws IllegalArgumentException when player name is empty
	 */
	public GameState(Tuple<Integer, Integer> boardPositions, Tuple<Integer, Integer> playerMoney, Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> playerNames, List<Patch> patches, boolean specialTileAvailable)
	{
		CheckUtil.assertNonNull(playerNames, boardPositions, playerMoney, patches, specialTileAvailable);
		CheckUtil.assertNonNull(playerNames.getFirst(), playerNames.getSecond());
		CheckUtil.assertNonNull(boardPositions.getFirst(), boardPositions.getSecond());
		CheckUtil.assertNonNull(playerMoney.getFirst(), playerMoney.getSecond());

		Tuple<String, PlayerType> firstPlayerNameAndType = playerNames.getFirst();
		Tuple<String, PlayerType> secondPlayerNameAndType = playerNames.getSecond();

		if(firstPlayerNameAndType.getFirst().isBlank() || secondPlayerNameAndType.getFirst().isBlank())
			throw new IllegalArgumentException("At least one player name is missing");

		this.specialTileAvailable = specialTileAvailable;


		player1 = new Player(boardPositions.getFirst(), playerMoney.getFirst(), firstPlayerNameAndType.getFirst(), firstPlayerNameAndType.getSecond());
		player2 = new Player(boardPositions.getSecond(), playerMoney.getSecond(), secondPlayerNameAndType.getFirst(), secondPlayerNameAndType.getSecond());

		timeBoard = new TimeBoardComponent[53];
		for(int i = 0; i < 53; i++)
		{
			timeBoard[i] = new TimeBoardComponent(i);
		}
		patches = new ArrayList<>();
		this.patches = patches;
	}

	/**
	 * Constructor for clones
	 *
	 * @param specialTileAvailable	true if the bonus tile is still available
	 * @param logEntry				change at move
	 * @param timeBoard				timeBoard Array
	 * @param patches				list of patches
	 * @param player1				first player
	 * @param player2				second player
	 */
	private GameState(boolean specialTileAvailable, String logEntry, TimeBoardComponent[] timeBoard, List<Patch> patches, Player player1, Player player2) {
		this.specialTileAvailable = specialTileAvailable;
		this.logEntry = logEntry;
		TimeBoardComponent[] arr = new TimeBoardComponent[timeBoard.length];
		for (int i = 0; i < timeBoard.length; i++) {
			arr[i] = timeBoard[i].copy();
		}
		this.timeBoard = arr;

		this.patches = patches.stream().map(Patch::copy).collect(Collectors.toList());
		this.player1 = player1.copy();
		this.player2 = player2.copy();
	}

	/**
	 * Checks if two gameStates are equal
	 * @param obj the Object to compare to
	 * @return true if objects are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}

		if (obj.getClass() == this.getClass())
		{
			GameState gameState = (GameState) obj;
			return 	specialTileAvailable == gameState.specialTileAvailable &&
					logEntry.equals(gameState.logEntry) &&
					Arrays.equals(timeBoard, gameState.timeBoard) &&
					patches.equals(gameState.patches) &&
					player1.equals(gameState.player1) &&
					player2.equals(gameState.player2);
		}
		return false;
	}

	/**
	 * Copies the GameState
	 * @return A copy of the GameState object
	 */
	public GameState copy() {
		return new GameState(specialTileAvailable, logEntry, timeBoard, patches, player1, player2);
	}

	/**
	 * protocols all game action of the current player
	 * @param logEntry player actions
	 */
	public void setLogEntry(String logEntry){this.logEntry = logEntry;}

	/**
	 * changes status of special tile availability
	 * @param specialTileAvailable info if tile is available
	 */
	public void setSpecialTileAvailable(boolean specialTileAvailable) { this.specialTileAvailable = specialTileAvailable; }

	/**
	 * changes the patches List
	 * @param patches list off all patches which are not taken by players yet
	 */
	public void setPatches(List<Patch> patches) { this.patches = patches; }

	/**
	 * Checks if special tile is still available
	 * @return true if special tile is still available
	 */
	public boolean getSpecialTileAvailable() {return specialTileAvailable;}

	/**
	 * returns the information of all actions made at this GameState
	 * @return player actions
	 */
	public String getLogEntry(){return logEntry;}

	/**
	 * Returns the list which represents the TimeBoard
	 * @return the Time Board
	 */
	public TimeBoardComponent[] getTimeBoard() { return timeBoard; }

	/**
	 * Returns all Patches which are still available
	 * @return list of patches
	 */
	public List<Patch> getPatches() { return patches; }

	/**
	 * Returns the first player
	 * @return first player object
	 */
	public Player getPlayer1() { return player1; }

	/**
	 * Returns the second player
	 * @return second player object
	 */
	public Player getPlayer2() { return player2; }
}
