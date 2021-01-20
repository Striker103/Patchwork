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

	private final ErrorAUI errorAUI;

	private final TurnAUI turnAUI;

	private final LogAUI logAUI;

	private final int boardPos = 53;

	/**
	 * Constructor that sets the mainController and all AUIs
	 *
	 * @param mainController The controller that knows all other controllers
	 * @param errorAUI the errorAUI
	 * @param turnAUI  the turnAUI
	 * @param logAUI  the logAUI
	 */
	public PlayerController(MainController mainController, ErrorAUI errorAUI, TurnAUI turnAUI, LogAUI logAUI){
		this.mainController = mainController;
		this.errorAUI = errorAUI;
		this.turnAUI = turnAUI;
		this.logAUI = logAUI;
	}

	private void getIncomeMovement(int playerPos, int steps, Player currentPlayer) {
		int income;
		if(playerPos + steps >= boardPos){
			income = boardPos - playerPos;
		}else{
			income = steps;
		}
		currentPlayer.setMoney(income);
		logAUI.updateLog(currentPlayer.getName() + " got " + income + " buttons from movement");
	}

	private void moveAndCheckPosition(int playerPos, int steps, Player currentPlayer, GameState currentGameState){
		TimeBoardComponent[] timeBoard = currentGameState.getTimeBoard();
		for(int i = playerPos+1; i <= playerPos + steps; i++){
			if(i <= boardPos){
				currentPlayer.setBoardPosition(i);
				if(timeBoard[i].hasPatch()){
					timeBoard[i].removePatch();
					logAUI.updateLog(currentPlayer.getName() + " stepped over a 1x1 Patch and has to place it now");
					get1x1Patch();
				}
				if(timeBoard[i].hasButton()){
					int income = calculateIncomePatches(currentPlayer);
					currentPlayer.setMoney(income);
					logAUI.updateLog(currentPlayer.getName() + " stepped over a Button and got " + income + " buttons");
				}
			}else{
				logAUI.updateLog(currentPlayer.getName() + " reached the end position ");
				break;
			}
		}
	}

	private void get1x1Patch(){
		turnAUI.triggerPlayerTurn();
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
	void moveTimeToken(Player currentPlayer, int steps) {
		Game game = mainController.getGame();
		int playerPos = currentPlayer.getBoardPosition();
		GameState currentGameState = game.getCurrentGameState();

		moveAndCheckPosition(playerPos, steps, currentPlayer,currentGameState);
		logAUI.updateLog(currentPlayer.getName() + " moved forward " + steps + " steps");
		getIncomeMovement(playerPos, steps, currentPlayer);
	}

	/**
	 * checks if the player has enough money. If so he pays the Patch
	 *
	 * @param patch the patch the player wants to buy
	 */
	void payPatch(Player currentPlayer, Patch patch) {
		if (currentPlayer.getMoney() < patch.getButtonsCost()) {
			errorAUI.showError("when you are to broke to buy a Patch <sad pikachu face>");
			return;
		}
		currentPlayer.setMinusMoney(patch.getButtonsCost());
		logAUI.updateLog(currentPlayer.getName() + " bought " + patch.getPatchID() + " for " + patch.getButtonsCost() + " buttons");
	}

}
