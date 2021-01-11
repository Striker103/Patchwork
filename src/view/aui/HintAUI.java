package view.aui;


import model.Patch;

public interface HintAUI {

	public abstract void showHintAdvance();

	public abstract void showHintTakePatch(Patch patch, boolean[][] placing);

}
