package view.aui;

public interface TurnAUI {

	public abstract void triggerPlayerTurn();

	public abstract  void trigger1x1Placement();

	public abstract void retriggerPatchPlacement();

	public abstract void updatePatches();

	public abstract void moveToken(String name, int time);
}
