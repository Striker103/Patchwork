package model;

import java.util.ArrayList;
import java.util.List;

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
	public GameState(Tuple<Integer, Integer> boardPositions, Tuple<Integer, Integer> playerMoney, Tuple<String, String> playerNames, List<Patch> patches, boolean specialTileAvailable)
	{
		CheckUtil.assertNonNull(playerNames, boardPositions, playerMoney, patches, specialTileAvailable);
		CheckUtil.assertNonNull(playerNames.getFirst(), playerNames.getSecond());
		CheckUtil.assertNonNull(boardPositions.getFirst(), boardPositions.getSecond());
		CheckUtil.assertNonNull(playerMoney.getFirst(), playerMoney.getSecond());

		if(playerNames.getFirst().isBlank() || playerNames.getSecond().isBlank())
			throw new IllegalArgumentException("At least one player name is missing");

		this.specialTileAvailable = specialTileAvailable;

		player1 = new Player(boardPositions.getFirst(), playerMoney.getFirst(), playerNames.getFirst());
		player2 = new Player(boardPositions.getSecond(), playerMoney.getSecond(), playerNames.getSecond());

		timeBoard = new TimeBoardComponent[53];
		for(int i = 0; i < 53; i++)
		{
			timeBoard[i] = new TimeBoardComponent(i);
		}
		this.patches = patches;
	}

	/**
	 * Constructor for clones
	 *
	 * @param gameState		The GameState to copy
	 */
	public GameState(GameState gameState) {
		player1 = new Player(gameState.player1);
		player2 = new Player(gameState.player2);

		patches = new ArrayList<>(gameState.patches);

		specialTileAvailable = gameState.specialTileAvailable;

		timeBoard = new TimeBoardComponent[53];
		for(int i = 0; i < gameState.timeBoard.length ; i++)
		{
			timeBoard[i] = gameState.timeBoard[i];
		}

		logEntry = gameState.getLogEntry();
	}

	/**
	 * Copies the GameState
	 * @return A copy of the GameState object
	 */
	@Override
	public GameState clone() {
		//TODO
		return null;
	}

	/**
	 * Returns the next player
	 * @return next player
	 */
	public Player getCurrentPlayer()
	{
		int positionOfFirstPlayer = player1.getBoardPosition();
		int positionOfSecondPlayer = player2.getBoardPosition();

		if(positionOfFirstPlayer < positionOfSecondPlayer || positionOfFirstPlayer == 0)
		{
			return player1;
		}
		else if(positionOfFirstPlayer > positionOfSecondPlayer)
		{
			return player2;
		}
		else
		{
			return null;//TODO If both time tokens are on the same space, the player whose token is on top goes first
		}
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
