package controller;

import model.*;
import view.aui.ErrorAUI;
import view.aui.LogAUI;
import view.aui.TurnAUI;
import java.util.List;

/**
 * gives the player income, moves his Token and pays for Patches
 *
 * @author Dennis
 */
public class PlayerController {

	private final MainController mainController;

	private ErrorAUI errorAUI;

	private TurnAUI turnAUI;

	private LogAUI logAUI;

	/**
	 * true if errorAUI is set
	 */
	private boolean errorAUIChanged = false;

	/**
	 * true if logAUIChanged is set
	 */
	private boolean logAUIChanged = false;

	/**
	 * true if turnAUIChanged is set
	 */
	private boolean turnAUIChanged = false;

	private final int boardPos = 53;

	/**
	 * Constructor that sets the mainController
	 *
	 * @param mainController The controller that knows all other controllers
	 */
	public PlayerController(MainController mainController){
		this.mainController = mainController;
	}

	private void getIncomeMovement(int playerPos, int steps, Player currentPlayer) {
		int income;
		if(playerPos + steps >= boardPos){
			income = boardPos - playerPos;
		}else{
			income = steps;
		}
		currentPlayer.addMoney(income);
		logAUI.updateLog(currentPlayer.getName() + " gets " + income + " buttons by advancing");
	}

	private void moveAndCheckPosition(int playerPos, int steps, Player currentPlayer, GameState currentGameState){
		TimeBoardComponent[] timeBoard = currentGameState.getTimeBoard();
		for(int i = playerPos+1; i <= playerPos + steps; i++){
			if(i <= boardPos){
				currentPlayer.setBoardPosition(i);
				if(timeBoard[i].hasPatch()){
					timeBoard[i].removePatch();
					logAUI.updateLog(currentPlayer.getName() + " steps over a 1x1 Patch and must place it now");
					get1x1Patch();
				}
				if(timeBoard[i].hasButton()){
					int income = calculateIncomePatches(currentPlayer);
					currentPlayer.addMoney(income);
					logAUI.updateLog(currentPlayer.getName() + " gets his button paycheck " + income + " buttons!");
				}
			}else{
				logAUI.updateLog(currentPlayer.getName() + " reached the end position ");
				break;
			}
		}
	}

	private void get1x1Patch(){
		turnAUI.trigger1x1Placement();
	}

	private int calculateIncomePatches(Player currentPlayer) {
		QuiltBoard quiltBoard = currentPlayer.getQuiltBoard();
		List<Patch> patches = quiltBoard.getPatches();
		int sum = 0;
		for(Patch patch : patches){
			sum += patch.getButtonIncome();
		}
		return sum;
	}

	/**
	 * moves the token of the player, gives him buttons for movement, gives him buttons if he stepped of a button and a 1x1Patch if he stepped over one
	 *
	 * @param steps number of steps the player makes
	 */
	void moveTimeToken(Player currentPlayer, int steps, boolean patchMovement) {
		Game game = mainController.getGame();
		int playerPos = currentPlayer.getBoardPosition();
		GameState currentGameState = game.getCurrentGameState();

		moveAndCheckPosition(playerPos, steps, currentPlayer,currentGameState);
		logAUI.updateLog(currentPlayer.getName() + " moves forward " + steps + " steps");
		if(!patchMovement)
			getIncomeMovement(playerPos, steps, currentPlayer);

		turnAUI.moveToken(currentPlayer.getName(), steps);
	}

	/**
	 * checks if the player has enough money. If so he pays the Patch
	 *
	 * @param patch the patch the player wants to buy
	 */
	void payPatch(Player currentPlayer, Patch patch) {
		currentPlayer.setMinusMoney(patch.getButtonsCost());
		logAUI.updateLog(currentPlayer.getName() + " buys Patch " + patch.getPatchID() + " for " + patch.getButtonsCost() + " buttons");
	}

	/**
	 * set the errorAUI
	 * @param errorAUI the errorAUI
	 */
	public void setErrorAUI(ErrorAUI errorAUI) {
		if(this.errorAUIChanged) throw new IllegalStateException("errorAUI was already set");
		this.errorAUI = errorAUI;
		this.errorAUIChanged = true;
	}

	/**
	 * set the logAUI
	 * @param logAUI the logAUI
	 */
	public void setLogAUI(LogAUI logAUI) {
		if(this.logAUIChanged) throw new IllegalStateException("logAUI was already set");
		this.logAUI = logAUI;
		this.logAUIChanged = true;
	}

	/**
	 * set the turnAUI
	 * @param turnAUI the turnAUI
	 */
	public void setTurnAUI(TurnAUI turnAUI) {
		if(this.turnAUIChanged) throw new IllegalStateException("turnAUI was already set");
		this.turnAUI = turnAUI;
		this.turnAUIChanged = true;
	}
}
