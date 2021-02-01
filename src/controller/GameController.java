package controller;


import model.*;
import view.aui.ErrorAUI;
import view.aui.LogAUI;
import view.aui.TurnAUI;

/**
 * The controller that executes turns
 * @author Yannick
 */
public class GameController {

	private static final int LASTBOARDPOSITION = 54;
	private final MainController mainController;

	private ErrorAUI errorAUI;

	private LogAUI logAUI;

	private TurnAUI turnAUI;

	/**
	 * true if errorAUI is changed
	 */
	private boolean errorAUIChanged = false;

	/**
	 * true if logAUI is changed
	 */
	private boolean logAUIChanged = false;

	/**
	 * true if logAUI is changed
	 */
	private boolean turnAUIChanged = false;

	/**
	 * Constructor that sets the mainController
	 *
	 * @param mainController The controller that knows all other controllers
	 */
	public GameController(MainController mainController){
		this.mainController = mainController;
	}

	/**
	 * Advance one field ahead of your opponent
	 */
	public void advance() {
		mainController.getUndoRedoController().clearRedoList();
		cloneGameState();

		Game game = mainController.getGame();
		int positionDifference = getNotMovingPlayer().getBoardPosition() - getNextPlayer().getBoardPosition();

		//is the second player not on the last field?
		if(!(game.getCurrentGameState().getTimeBoard().length-1 ==  getNotMovingPlayer().getBoardPosition())){
			//move one field more
			positionDifference++;
		}
		//move
		mainController.getPlayerController().moveTimeToken(getNextPlayer(),positionDifference, false);
		endTurn();
	}

	/**
	 * Take and pay the patch. check if it fits, the player has enough money and fills a 7x7 area
	 *
	 * @param patch    the patch
	 * @param placing  the placing
	 * @param rotation the rotation
	 * @param flipped  the flipped
	 */
	public void takePatch(Patch patch, Matrix placing,int rotation,boolean flipped) {
		Player executingPlayer = getNextPlayer();
		QuiltBoard playerBoard = executingPlayer.getQuiltBoard();
		Matrix playerPatchBoard = playerBoard.getPatchBoard();

		if(placing.getRows() != playerPatchBoard.getRows() || placing.getColumns() != playerPatchBoard.getColumns()){
			throw new IllegalStateException("The placing needs to be the same size as the quiltboards!");
		}
		//The patch cant be placed there
		if(!playerPatchBoard.disjunctive(placing)){
			turnAUI.reTriggerPatchPlacement();
			return;
		}

		if (executingPlayer.getMoney() < patch.getButtonsCost()) {
			errorAUI.showError("when you are to broke to buy a Patch <sad pikachu face>");
			endTurn();
			return;
		}
		//Clone the GameState
		mainController.getUndoRedoController().clearRedoList();
		cloneGameState();
		executingPlayer = getNextPlayer();
		playerBoard = executingPlayer.getQuiltBoard();
		playerPatchBoard = playerBoard.getPatchBoard();


		//Paying of patch
		mainController.getPlayerController().payPatch(executingPlayer,patch);

		mainController.getGame().getCurrentGameState().tookPatch(patch);



		//Placement of patch
		playerBoard.addPatch(patch,placing,rotation,flipped);

		//Distribution of the special tile
		if(mainController.getGame().getCurrentGameState().specialTileAvailable()&&playerBoard.check7x7()){//TODO check7x7 implementieren
			executingPlayer.setHasSpecialTile(true);
		}
		//Moving on timeboard
		mainController.getPlayerController().moveTimeToken(executingPlayer,patch.getTime(), true);
		endTurn();

	}

	/**
	 * Places a 1x1 Patch at specified position
	 *
	 * @param xPosition The column of the position
	 * @param yPosition The row of the position
	 * @param player    The Player who placed the patch
	 */
	public boolean place1x1Patch(int xPosition, int yPosition, Player player){
		boolean placed = false;
		try{
			placed = player.getQuiltBoard().add1x1Patch(xPosition,yPosition);
		}catch (IllegalArgumentException e){
			errorAUI.showError(e.getMessage());
		}
		return placed;
	}

	/**
	 * Returns the next moving player
	 *
	 * @return the player furthest behind on the board
	 */
	public Player getNextPlayer(){
		return getNextPlayer(mainController.getGame().getCurrentGameStateIndex());
	}

	/**
	 * The Player that is not moving
	 *
	 * @return the player with the higher board position
	 */
	public Player getNotMovingPlayer(){
		Player movingPlayer = getNextPlayer();
		GameState currentGS = mainController.getGame().getCurrentGameState();
		if(currentGS.getPlayer1().getName().equals(movingPlayer.getName())){
			return currentGS.getPlayer2();
		}else{
			return currentGS.getPlayer1();
		}
	}

	/**
	 * Triggers the next Human or AI turn
	 */
	void endTurn() {
		GameState currentGameState = mainController.getGame().getCurrentGameState();
		if(currentGameState.getPlayer1().getBoardPosition()==LASTBOARDPOSITION && currentGameState.getPlayer2().getBoardPosition()==LASTBOARDPOSITION){
			return;
		}
		Player nextMovingPlayer = getNextPlayer();
		AIController aiController = mainController.getAIController();
		if(nextMovingPlayer.getPlayerType()== PlayerType.HUMAN){
			turnAUI.triggerPlayerTurn();
		}else {
			aiController.doTurn();
		}
	}

	/**
	 * Makes a copy of the current GameState and appends it
	 */
	void cloneGameState(){
		mainController.getGame().addGameState(mainController.getGame().getCurrentGameState().copy());
	}

	/**
	 * Looks which player is behind. If both have an equal Position, look who came last to that field
	 * @param gameStateIndex The Gamestate to calculate Player position
	 * @return next moving Player
	 */
	Player getNextPlayer(int gameStateIndex) {
		GameState gameState = mainController.getGame().getGameStates().get(gameStateIndex);
		Player player1 = gameState.getPlayer1();
		Player player2 = gameState.getPlayer2();


		if(player1.getBoardPosition()<player2.getBoardPosition()||player1.getBoardPosition()==0){
			return player1;
		}else if(player1.getBoardPosition()>player2.getBoardPosition()||player2.getBoardPosition()==0 ){
			return player2;
		}else{
			if(getNextPlayer(gameStateIndex-1).getName().equals(gameState.getPlayer1().getName())){
				return gameState.getPlayer1();
			}else{
				return gameState.getPlayer2();
			}
		}
	}

	/**
	 * set the errorAUI
	 *
	 * @param errorAUI the errorAUI
	 */
	public void setErrorAUI(ErrorAUI errorAUI) {
		if(this.errorAUIChanged) throw new IllegalStateException("errorAUI was already set");
		this.errorAUI = errorAUI;
		this.errorAUIChanged = true;
	}

	/**
	 * set the logAUI
	 *
	 * @param logAUI the logAUI
	 */
	public void setLogAUI(LogAUI logAUI) {
		if(this.logAUIChanged) throw new IllegalStateException("logAUI was already set");
		this.logAUI = logAUI;
		this.logAUIChanged = true;
	}

	/**
	 * set the turnAUi
	 *
	 * @param turnAUI the turnAUI
	 */
	public void setTurnAUI(TurnAUI turnAUI) {
		if(this.turnAUIChanged) throw new IllegalStateException("turnAUI was already set");
		this.turnAUI = turnAUI;
		this.turnAUIChanged = true;
	}
}
