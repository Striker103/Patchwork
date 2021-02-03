package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
	 * Constructor for a new GameState Object. Starting position for the players is boardPosition 0. Each player gets 5 start Buttons.
	 * This constructor is only called once when a game starts
	 *
	 * @param playerNames 			names and types of players
	 * @param patches 				list of patches
	 *
	 * @throws IllegalArgumentException when player name is empty
	 */
	public GameState(Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> playerNames, List<Patch> patches, boolean ironman)
	{
		CheckUtil.assertNonNull(playerNames, patches);
		CheckUtil.assertNonNull(playerNames.getFirst(), playerNames.getSecond());

		String player1Name = playerNames.getFirst().getFirst();
		PlayerType player1Type = playerNames.getFirst().getSecond();
		String player2name = playerNames.getSecond().getFirst();
		PlayerType player2Type = playerNames.getSecond().getSecond();
		Score player1Score = new Score(0, ironman, player2Type, player1Name);
		Score player2Score = new Score(0, ironman, player1Type, player2name);

		if(player1Name.isBlank() || player2name.isBlank())
			throw new IllegalArgumentException("At least one player name is missing");

		player1 = new Player( player1Name, player1Type, player1Score);
		player2 = new Player( player2name, player2Type, player2Score);

		timeBoard = new TimeBoardComponent[54];
		for(int i = 0; i < timeBoard.length; i++)
		{
			timeBoard[i] = new TimeBoardComponent(i);
		}
		this.patches = patches.stream().map(Patch::copy).collect(Collectors.toList());
		this.logEntry = "";
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
	 * Returns patch with the specified ID
	 * @param patchid ID
	 * @return patch with this ID
	 * @throws java.util.NoSuchElementException if no patch with this ID exists
	 */
	public Patch getPatchByID(final int patchid){
		//noinspection OptionalGetWithoutIsPresent
		return patches.stream().filter(patch -> patch.getPatchID() == patchid).findFirst().get();
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


	/**
	 * Whether or not not the special tile is available in this gamestate
	 * @return availability of the special tile
	 */
	public boolean specialTileAvailable(){
		return !player1.getHasSpecialTile() && !player2.getHasSpecialTile();
	}

	/**
	 * Removes the takeOutPatch from the patches list
	 * @param takeOutPatch the patch to take out of the list
	 */
	public void takePatchOutOfPatchList(Patch takeOutPatch){
		patches.remove(takeOutPatch);
	}

	/**
	 * Gets the patches that could be placed by the player
	 * @return the first three patches of the patch list
	 */
	public Patch[] getNext3Patches(){
		Patch[] nextPatches = new Patch[3];
		for (int i=0;i<3;i++){
			try {
				nextPatches[i] = patches.get(i);
			}catch (Exception e){
				nextPatches[i] = null;
			}
		}
		return nextPatches;
	}

	/**
	 * Remove the choosen Patch by index and append all patches before
	 * @param index the index of the patch from 0-2
	 */
	public void tookPatch(int index){
		if(index>2||index<0){
			throw new IllegalArgumentException("Please give the choosen Patch (0-2)");
		}
		for(int i = 0;i<index;i++){
			Patch indexPatch = patches.get(0);
			patches.remove(0);
			patches.add(indexPatch);
		}
		patches.remove(0);
	}
	/**
	 * Remove the chosen Patch by object and append all patches before
	 * @param chosenPatch the patch by object reference
	 */
	public void tookPatch(Patch chosenPatch){
		if(patches.indexOf(chosenPatch)>2){
			throw new IllegalArgumentException("Please choose one of the first three patches");
		}
		for(int i = 0;i<patches.indexOf(chosenPatch) + 1;i++){
			Patch indexPatch = patches.get(0);
			patches.remove(0);
			patches.add(indexPatch);
		}


		patches.remove(0);

	}


}
