package ai;


import model.GameState;
import model.Player;

/**
 * @author Lukas Kidin
 */
public abstract class AI {

	/**
	 * Calculates turn
	 *
	 * @param actualState game state to calculate the turn from
	 * @param movingPlayer player to calculate the turn from
	 * @return calculated turn as game state
	 */
	public abstract GameState calculateTurn(GameState actualState, Player movingPlayer);

}
