package model;

public class Patch {

	private final int buttonIncome;

	private final int buttonsCost;

	private final boolean[][] shape;

	private final int time;

	private int id;

	public Patch(int buttonIncome, int buttonsCost, boolean[][] shape, int time) {
		this.buttonIncome = buttonIncome;
		this.buttonsCost = buttonsCost;
		this.shape = shape;
		this.time = time;
	}
	@Override
	public Patch clone() {
		return null;
	}

}
