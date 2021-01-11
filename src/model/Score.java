package model;

import java.time.LocalDateTime;

public class Score {

	private int value;

	private LocalDateTime date;

	private boolean isIronman;

	private PlayerType opponentType;

	private String playerName;

	@Override
	public Score clone() {
		return null;
	}

}
