package com.puzzlegalaxy.slider;

import java.util.UUID;

public class Level {

	private LevelType levelType;
	private Object[][] savedInfo;
	private int levelNum, steps;
	private int stepsTaken = 0;
	private int[] gSequence;
	private String levelName, expression;
	private boolean solved;
	private UUID id;
	
	public Level(LevelType levelType, Object[][] savedInfo, int[] gSec, int levelNum, int steps, String levelName, String expression, boolean solved, UUID id) {
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
	
	public Level(LevelType levelType, int[] gSec, int levelNum, int steps, String levelName, String expression, UUID id) {
		this(levelType, new Object[11][11], gSec, levelNum, steps, levelName, expression, false, id);
	}
	
	public Level(LevelType levelType, int[] gSec, int levelNum, int steps, String expression, UUID id) {
		this(levelType, new Object[11][11], gSec, levelNum, steps, "Level %n", expression, false, id);
	}
	
	public Level(LevelType levelType, int[] gSec, int levelNum, int steps, String levelName, String expression) {
		this(levelType, new Object[11][11], gSec, levelNum, steps, levelName, expression, false, UUID.randomUUID());
	}
	
	public Level(LevelType levelType, int[] gSec, int levelNum, int steps, String expression) {
		this(levelType, new Object[11][11], gSec, levelNum, steps, "Level %n", expression, false, UUID.randomUUID());
	}
	
	public Level(LevelType levelType, int levelNum, int steps, String levelName, String expression, UUID id) {
		this(levelType, new Object[11][11], new int[steps], levelNum, steps, levelName, expression, false, id);
	}
	
	public Level(LevelType levelType, int levelNum, int steps, String expression, UUID id) {
		this(levelType, new Object[11][11], new int[steps], levelNum, steps, "Level %n", expression, false, id);
	}
	
	public Level(LevelType levelType, int levelNum, int steps, String levelName, String expression) {
		this(levelType, new Object[11][11], new int[steps], levelNum, steps, levelName, expression, false, UUID.randomUUID());
	}
	
	public Level(LevelType levelType, int levelNum, int steps, String expression) {
		this(levelType, new Object[11][11], new int[steps], levelNum, steps, "Level %n", expression, false, UUID.randomUUID());
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
	
}
