package com.puzzlegalaxy.slider;

import java.util.UUID;

public class Level {

	private LevelType levelType;
	private Object[][] savedInfo;
	private int levelNum, steps;
	private int stepsTaken = 0;
	private int[][] gSequence;
	private String levelName, expression;
	private boolean solved, needsRefresh;
	private UUID id;
	
	public Level(LevelType levelType, Object[][] savedInfo, int[][] gSec, int levelNum, int steps, String levelName, String expression, boolean solved, UUID id) {
		this.levelType = levelType;
		this.savedInfo = savedInfo;
		this.levelNum = levelNum;
		this.steps = steps;
		this.gSequence = gSec;
		this.levelName = levelName;
		this.expression = expression;
		this.solved = solved;
		this.id = id;
	}
	
	public Level(LevelType levelType, int[][] gSec, int levelNum, int steps, String levelName, String expression, UUID id) {
		this(levelType, null, gSec, levelNum, steps, levelName, expression, false, id);
	}
	
	public Level(LevelType levelType, int[][] gSec, int levelNum, int steps, String expression, UUID id) {
		this(levelType, null, gSec, levelNum, steps, "Level %n", expression, false, id);
	}
	
	public Level(LevelType levelType, int[][] gSec, int levelNum, int steps, String levelName, String expression) {
		this(levelType, null, gSec, levelNum, steps, levelName, expression, false, UUID.randomUUID());
	}
	
	public Level(LevelType levelType, int[][] gSec, int levelNum, int steps, String expression) {
		this(levelType, null, gSec, levelNum, steps, "Level %n", expression, false, UUID.randomUUID());
	}
	
	public Level(LevelType levelType, int levelNum, int steps, String levelName, String expression, UUID id) {
		this(levelType, null, new int[steps][10], levelNum, steps, levelName, expression, false, id);
	}
	
	public Level(LevelType levelType, int levelNum, int steps, String expression, UUID id) {
		this(levelType, null, new int[steps][10], levelNum, steps, "Level %n", expression, false, id);
	}
	
	public Level(LevelType levelType, int levelNum, int steps, String levelName, String expression) {
		this(levelType, null, new int[steps][10], levelNum, steps, levelName, expression, false, UUID.randomUUID());
	}
	
	public Level(LevelType levelType, int levelNum, int steps, String expression) {
		this(levelType, null, new int[steps][10], levelNum, steps, "Level %n", expression, false, UUID.randomUUID());
	}

	public LevelType getLevelType() {
		return this.levelType;
	}

	public Object[][] getSavedInfo() {
		return this.savedInfo;
	}

	public int getLevelNum() {
		return this.levelNum;
	}

	public String getLevelName() {
		if (this.levelName == null)
			this.levelName = "Level %n";
		return this.levelName.replace("%n", levelNum + "");
	}

	public UUID getID() {
		return id;
	}

	public int getStepsTaken() {
		return stepsTaken;
	}

	public int[][] getgSequence() {
		return gSequence;
	}

	public String getExpression() {
		return expression;
	}
	
	public int getSteps() {
		return this.steps;
	}

	public boolean doesNeedsRefresh() {
		return needsRefresh;
	}
	
	public boolean isSolved() {
		return solved;
	}

	public void setSavedInfo(Object[][] savedInfo) {
		this.savedInfo = savedInfo;
	}

	public void setLevelNum(int levelNum) {
		this.levelNum = levelNum;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public boolean stepValid(int step) {
		if (this.steps > this.stepsTaken++) {
			if (this.levelType != LevelType.CALCULATED)
				return false;
		}
		if (this.levelType == LevelType.RANDOM) {
			int[] section = this.gSequence[this.stepsTaken++];
			int count = 0;
			for (int i : section) {
				if (i == -1)
					continue;
				if (step == count)
					return true;
				count++;
			}
		} else if (this.levelType == LevelType.SAVED) {
			
		} else {
			
		}
		return true;
	}
	
	public int getComputerMove(int step) {
		
	}
	
	/**
	 * @deprecated	Found that it would mess with the gSequence so i
	 * 				replaced it with an updated method that also handles
	 * 				the gSequence
	 */
	public void nextStep() {
		this.stepsTaken += 1;
	}
	
	public int nextStep(int previous) {
		
	}
	
	public void undo(int steps) {
		this.stepsTaken -= steps;
		this.stepsTaken = this.stepsTaken < 0 ? 0 : this.stepsTaken;
	}
	
	public int getStepSetup() {
		return this.stepsTaken == 0 ? 0 : this.gSequence[this.stepsTaken--];
	}
	
}
