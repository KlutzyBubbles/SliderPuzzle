package com.puzzlegalaxy.slider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.puzzlegalaxy.slider.exceptions.InvalidExpressionException;
import com.puzzlegalaxy.slider.exceptions.InvalidLevelException;

public class LevelManager {

	private List<Level> levels = new ArrayList<Level>();

	public LevelManager(List<Level> levels) {
		this.levels = levels;
	}

	public LevelManager() {
		this(new ArrayList<Level>());
	}

	/**
	 * Removes all null values in the main list, this prevents any NPE
	 * when using the objects.
	 * 
	 * @return	TRUE: Null objects have been found and removed. FALSE: The list is fine
	 */
	private boolean removeNull() {
		int count = 0;
		boolean removed = false;
		for (Level l : new ArrayList<Level>(this.levels)) {
			if (l == null) {
				Main.debug("Null level found at index: " + count);
				levels.remove(l);
				removed = true;
			}
			count++;
		}
		return removed;
	}

	/**
	 * Checks if there is a Level that exists with the same level number
	 * NOTE: if there are levels with the same number this method only
	 * 		 returns the first of them.
	 * 
	 * @param levelNum	The number of the level you are checking
	 * @return			TRUE: A Level with that number exists. FALSE: No Level with that number exists
	 */
	public boolean levelExists(int levelNum) {
		if (this.levels.size() == 0 || levels.isEmpty()) {
			Main.debug("levelExists(int): Levels list is empty");
			return false;
		}
		boolean nullVals = this.removeNull();
		if (nullVals)
			Main.debug("levelExists(int): Null levels existed (refer to above)");
		int count = 0;
		for (Level l : this.levels) {
			if (l.getLevelNum() == levelNum) {
				Main.debug("levelExists(int): Level exists at index: " + count);
				return true;
			}
			count++;
		}
		Main.debug("levelExists(int): Level doesnt exist with " + this.levels.size() + " Levels inside the list");
		return false;
	}

	/**
	 * Returns the FIRST instance of a level that has the same level number.
	 * 
	 * @param levelNum	The number of the level you want to get
	 * @return			Level: The level object. NULL: a null object if the level doesn't exist
	 * @see				Level
	 */
	public Level getLevel(int levelNum) {
		if (!this.levelExists(levelNum)) {
			Main.debug("getLevel(int): Level doesnt exist");
			return null;
		}
		boolean nullVals = this.removeNull();
		if (nullVals)
			Main.debug("getLevel(int): Null levels existed (refer to above)");
		int count = 0;
		for (Level l : this.levels) {
			if (l.getLevelNum() == levelNum) {
				Main.debug("getLevel(int): Level exists at index: " + count);
				return l;
			}
			count++;
		}
		Main.debug("getLevel(int): WARNING - This message means there has been a logic error");
		return null;
	}

	/**
	 * Checks if there is a Level that exists with the same level number
	 * NOTE: if there are levels with the same number this method only
	 * 		 returns the first of them.
	 * 
	 * @param id	The id of the level you are checking
	 * @return		TRUE: A Level with that id exists. FALSE: No Level with that id exists
	 */
	public boolean levelExists(UUID id) {
		if (this.levels.size() == 0 || levels.isEmpty()) {
			Main.debug("levelExists(UUID): Levels list is empty");
			return false;
		}
		boolean nullVals = this.removeNull();
		if (nullVals)
			Main.debug("levelExists(UUID): Null levels existed (refer to above)");
		int count = 0;
		for (Level l : this.levels) {
			if (l.getID() == id) {
				Main.debug("levelExists(UUID): Level exists at index: " + count);
				return true;
			}
			count++;
		}
		Main.debug("levelExists(UUID): Level doesnt exist with " + this.levels.size() + " Levels inside the list");
		return false;
	}

	/**
	 * Returns the FIRST instance of a level that has the same level number.
	 * 
	 * @param id	The id of the level you want to get
	 * @return		Level: The level object. NULL: a null object if the level doesn't exist
	 * @see			Level
	 */
	public Level getLevel(UUID id) {
		if (!this.levelExists(id)) {
			Main.debug("getLevel(int): Level doesnt exist");
			return null;
		}
		boolean nullVals = this.removeNull();
		if (nullVals)
			Main.debug("getLevel(int): Null levels existed (refer to above)");
		int count = 0;
		for (Level l : this.levels) {
			if (l.getID() == id) {
				Main.debug("getLevel(int): Level exists at index: " + count);
				return l;
			}
			count++;
		}
		Main.debug("getLevel(int): WARNING - This message means there has been a logic error");
		return null;
	}

	/**
	 * Splits a raw level string into a String[] containing the level information and level matrix.
	 * NOTE: Can throw InvalidLevelException if the string is not a valid level.
	 * 
	 * @param raw						The raw string to interpret
	 * @return							The String[] object containing the split values
	 * @throws InvalidLevelException	Thrown if the string is not that of a Level string
	 */
	public String[] splitRaw(String raw) throws InvalidLevelException {
		String[] val = new String[2];
		String[] split = raw.split(",");
		if (split.length < 7) {
			throw new InvalidLevelException("The specified level doesnt have enough data to parse (Code: 0)");
		}
		LevelType l = LevelType.DEFAULT;
		try {
			l = LevelType.valueOf(split[3]);
		} catch (IllegalArgumentException e) {
			throw new InvalidLevelException("The specified level doesnt have a correct LevelType (Code: 1)");
		}
		val[0] = val[1] = "";
		if (l == LevelType.SAVED) {
			for (int i = 0; i < 6; i++) {
				val[0] += "," + split[i];
			}
			for (int i = 6; i < split.length; i++) {
				val[1] += "," + split[i];
			}
		} else {
			for (int i = 0; i < 6; i++) {
				val[0] += "," + split[i];
			}
			val[1] = split[6];
 		}

		if (val[0].startsWith(",")) {
			val[0] = val[0].replaceFirst(",", "");
		}
		if (val[1].startsWith(",")) {
			val[1] = val[1].replaceFirst(",", "");
		}
		Main.debug("CUT: " + val[0]);
		return val;
	}

	public int[] intArrFromString(String s) throws InvalidLevelException {
		s = s.replace(" ", "");
		Main.debug(s);
		if (s.equals("[]")) {
			return new int[10];
		}
		if (!s.contains("]") || !s.contains("[") || !s.contains(",")) {
			throw new InvalidLevelException("The specified string is not an int[] (Code: 2)");
		}
		String[] split = s.replace("[", "").replace("]", "").split(",");
		int[] val = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			try {
				val[i] = Integer.parseInt(split[i]);
			} catch (NumberFormatException e) {
				throw new InvalidLevelException("There is an unknown variable in the int[] (Code: 3)");
			}
		}
		return val;
	}

	public Object[][] objArrFromString(String s) throws InvalidLevelException {
		if (!s.contains("}") || !s.contains("{") || !s.contains("]") || !s.contains("[") || !s.contains(",")) {
			throw new InvalidLevelException("The specified string is not an valid matrix (Code: 8)");
		}
		String[] splitY = s.replace("{", "").replace("}", "").replace("]", "").split("\\[");
		Object[][] val = new Object[splitY.length][splitY[0].split(",").length];
		for (int i = 0; i < splitY.length; i++) {
			String[] splitX = splitY[i].split(",");
			if (splitX.length >= val.length) {
				for (int ii = 0; ii < splitX.length; ii++) {
					Main.debug("[" + ii + "," + i + "]");
					if (i == 0 && ii == 0) {
						val[ii][i] = splitX[ii];
						continue;
					}
					Main.debug("NUM: " + splitX[ii]);
					try {
						Integer.parseInt(splitX[ii]);
					} catch (NumberFormatException e) {
						throw new InvalidLevelException("Only value [0,0] of the matrix can be an Object (Code: 9)");
					}
					val[ii][i] = splitX[ii];
				}
			}
		}
		return val;
	}

	public boolean validateLevel(String[] level) throws InvalidLevelException {
		if (level == null)
			return false;
		Main.debug("LENGTH: " + level.length);
		if (level.length != 2)
			return false;
		String[] param = level[0].split(",");
		Main.debug("PLENGTH: " + param.length);
		if (param.length < 6)
			return false;
		Main.debug("TEST: " + param[0]);
		try {
			Integer.parseInt(param[0]);
		} catch (NumberFormatException e) {
			throw new InvalidLevelException("The specified level doesnt have a number (Code: 4)");
		}
		LevelType l = LevelType.DEFAULT;
		try {
			l = LevelType.valueOf(param[3]);
		} catch (IllegalArgumentException e) {
			throw new InvalidLevelException("The specified level doesnt have a correct LevelType (Code: 1)");
		}
		try {
			Integer.parseInt(param[4]);
		} catch (NumberFormatException e) {
			throw new InvalidLevelException("The specified level doesnt have steps (Code: 5)");
		}
		this.intArrFromString(param[5]);
		if (level[1] == null) {
			throw new InvalidLevelException("The custom level matrix is null (Code: 6)");
		}
		if (level[1].isEmpty() || level[1].length() == 0) {
			throw new InvalidLevelException("The custom level matrix is empty (Code: 7)");
		}
		if (l == LevelType.SAVED) {
			this.objArrFromString(level[1]);
		} else {
			String exp = level[1].replace("\\{", "").replace("\\}", "").replace("]", "").replace("[", "");
			Main.debug("validateLevel(String[]): EXP: " + exp);
			Equation e = new Equation(exp, 1);
			try {
				e.evaluate();
			} catch (InvalidExpressionException ex) {
				ex.printStackTrace();
				throw new InvalidLevelException("The equation cannot be computed (Code: 10)");
			}
			if (!e.isValid())
				throw new InvalidLevelException("The equation cannot be computed (Code: 10)");
		}
		return true;
	}

	public Level addLevel(String[] level, UUID uuid) throws InvalidLevelException {
		if (!this.validateLevel(level)) {
			throw new InvalidLevelException("The string validation failed (Code: Unknown)");
		}
		String[] param = level[0].split(",");
		int levelNum = Integer.parseInt(param[0]);
		int steps = Integer.parseInt(param[4]);
		String levelName = param[1];
		boolean solved = Boolean.getBoolean(param[2]);
		LevelType l = LevelType.valueOf(param[3]);
		int[] gSec = this.intArrFromString(param[5]);
		Level value;
		if (l == LevelType.SAVED) {
			value = new Level(l, this.objArrFromString(level[1]), gSec, levelNum, steps, levelName, "", solved, uuid);
		} else {
			value = new Level(l, gSec, levelNum, steps, levelName, level[1], uuid);
		}
		this.levels.add(value);
		return value;
	}

	public Level addLevel(int levelNum, String levelName) {
		Level value = new Level(LevelType.RANDOM, levelNum, 10, levelName, "");
		this.levels.add(value);
		return value;
	}

	public Level addLevel(int levelNum) {
		return this.addLevel(levelNum, "Level %n");
	}

	public Level addDefaultLevel(int levelNum, String levelName) {
		Level value = new Level(LevelType.DEFAULT, levelNum, 10, levelName, "");
		this.levels.add(value);
		return value;
	}

	public Level addDefaultLevel(int levelNum) {
		return this.addDefaultLevel(levelNum, "Level %n");
	}

	public int getLevelCount() {
		return this.levels.size();
	}

	public void printLevels() {
		if (this.levels == null)
			return;
		if (this.levels.isEmpty())
			return;
		for (Level l : this.levels) {
			System.out.println(l.getID().toString() + "," + l.getLevelNum() + "," + l.getLevelType().toString() + "," + l.getLevelName());
		}
	}

	public void loadLevels() {
		File folder = new File("src/levels/");
		File[] files = folder.listFiles();
		Main.debug("loadLevels(): " + files.length + " length");
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory())
				continue;
			if (!f.getName().endsWith(".level"))
				continue;
			String level = "";
			try {
				BufferedReader r = new BufferedReader(new FileReader(f));
				String line;
				while ((line = r.readLine()) != null) {
					level += line;
				}
				r.close();
				if (level.equals("")) {
					Main.debug("loadLevels(): File has nothing in it");
					continue;
				}
				String[] split = this.splitRaw(level);
				this.addLevel(split, UUID.fromString(f.getName().split("\\.")[0]));
			} catch (FileNotFoundException e) {
				Main.debug("loadLevels(): File could not be found after loading");
			} catch (IOException e) {
				Main.debug("loadLevels(): File could not be read");
			} catch (InvalidLevelException e) {
				e.printStackTrace();
				Main.debug("loadLevels(): File contained an invalid level (see above)");
			}
		}
	}

}
