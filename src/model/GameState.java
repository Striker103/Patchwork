package model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dennis Querndt
 * GameState is an Object which holds all informations regarding the current player move. Every player move has its own GameState
 */
public class GameState{

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
	 * Constructor for a new GameState Object. Starting position for the players is boardPosition 0
	 *
	 * @param playerNames 			names and types of players
	 * @param patches 				list of patches
	 *
	 * @throws IllegalArgumentException when player name is empty
	 */
	public GameState(Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> playerNames, List<Patch> patches)
	{
		CheckUtil.assertNonNull(playerNames, patches);
		CheckUtil.assertNonNull(playerNames.getFirst(), playerNames.getSecond());

		Tuple<String, PlayerType> firstPlayerNameAndType = playerNames.getFirst();
		Tuple<String, PlayerType> secondPlayerNameAndType = playerNames.getSecond();

		if(firstPlayerNameAndType.getFirst().isBlank() || secondPlayerNameAndType.getFirst().isBlank())
			throw new IllegalArgumentException("At least one player name is missing");



		player1 = new Player(0, 5, firstPlayerNameAndType.getFirst(), firstPlayerNameAndType.getSecond(), false);
		player2 = new Player(0, 5, secondPlayerNameAndType.getFirst(), secondPlayerNameAndType.getSecond(), false);

		timeBoard = new TimeBoardComponent[54];
		for(int i = 0; i < timeBoard.length; i++)
		{
			timeBoard[i] = new TimeBoardComponent(i);
		}
		this.patches = patches.stream().map(Patch::copy).collect(Collectors.toList());
	}

	/**
	 * Constructor for clones
	 *
	 * @param logEntry				change at move
	 * @param timeBoard				timeBoard Array
	 * @param patches				list of patches
	 * @param player1				first player
	 * @param player2				second player
	 */
	private GameState(String logEntry, TimeBoardComponent[] timeBoard, List<Patch> patches, Player player1, Player player2) {
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
			return logEntry.equals(gameState.logEntry) &&
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
		return new GameState( logEntry, timeBoard, patches, player1, player2);
	}

	/**
	 * protocols all game action of the current player
	 * @param logEntry player actions
	 */
	public void setLogEntry(String logEntry){this.logEntry = logEntry;}


	/**
	 * changes the patches List
	 * @param patches list off all patches which are not taken by players yet
	 */
	public void setPatches(List<Patch> patches) { this.patches = patches; }


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

	/**
	 * Deprecated. Please use getNextPlayer in GameController. If questions arise, contact me (Yannick)
	 */
	@Deprecated
	public Player nextPlayer(){
		throw new IllegalStateException("Methode deprecated. Please use getNextPlayer from GameController");
	}


}
