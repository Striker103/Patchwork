package ai;


import model.GameState;
import model.Player;

public abstract class AI {

	public abstract GameState calculateTurn(GameState actualState, Player movingPlayer);

}
