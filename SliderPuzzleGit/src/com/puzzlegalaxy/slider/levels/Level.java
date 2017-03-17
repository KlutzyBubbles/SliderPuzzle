package com.puzzlegalaxy.slider.levels;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import com.puzzlegalaxy.slider.Main;
import com.puzzlegalaxy.slider.exceptions.InvalidExpressionException;
import com.puzzlegalaxy.slider.exceptions.InvalidLevelException;
import com.puzzlegalaxy.slider.utils.ArrayUtils;
import com.puzzlegalaxy.slider.utils.Equation;

public class Level implements Cloneable {

	/**
	 * canBeNull: No, null values will default to LevelType.DEFAULT
	 * Restrictions:
	 * 	- Must be of type LevelType Enum
	 * 	- Cannot be changed after level initialization
	 * Note:
	 * 
	 * This value is heavily depended on and if at any stage it is illegally changed
	 * altered or removed the level will fail to operate
	 */
	private LevelType levelType;

	/**
	 * canBeNull: Yes, only when levelType does NOT equal LevelType.SAVED
	 * Restrictions: 
	 * 	- Only the object [0, 0] can be anything other than an int
	 * 	- The size of the object (subject to change) can be up to Object[Integer.MAX_VALUE][10]
	 * Note:
	 * 
	 * When Object[][] = Object[x][y] : 
	 * 	- The x marks the step number
	 *  - The y marks the value the user inputs
	 * 	- The [x, y] marks the value to check against
	 * 
	 * If the player selects 7 on their 3rd step the value chosen will be:
	 * Object[3][7] - Because the values in the column and row 0 mark headers
	 * the values of the table are located 1 inside meaning the table starts
	 * at 1 instead of 0. 
	 */
	private Object[][] savedInfo;

	/**
	 * canBeNull: No, primitive type
	 * Restrictions:
	 * 	- levelNum: None
	 * 	- steps: Anything below 1 will be marked as unlimited steps
	 * 	- previousStep: anything not between 0-9 will be marked as invalid and may cause a level reset
	 * Note:
	 * 
	 * All values are defaulted to -1 which is a recommended invalid value for each variable
	 * 
	 * levelNum:		The display number of the level, free to change or get at any time as it is a display item only
	 * steps:			The total amount of steps that can be taken in a level (Ignored when levelType equals LevelType.SAVED)
	 * previousStep:	The previous step taken, used when getting a step setup after an undo or step change
	 */
	private int levelNum, steps, previousStep = -1;

	/**
	 * canBeNull: No, primitive type
	 * Restrictions:
	 * 	- stepsTaken: Cannot be below 0
	 *  - stepsTaken: Not to be edited directly
	 *  - choices: must be greater than 0 when levelType equals LevelType.RANDOM
	 *  Note:
	 *  
	 *  stepsTaken: if this is to be edited directly, the level may not function as
	 *  expected as there are other variables the level has to handle at the same time.
	 *  choices: if this value is seen to be invalid when generating the level it will default to 1
	 */
	private int stepsTaken, choices = 0;

	/**
	 * canBeNull: No, primitive type
	 * Restrictions:
	 *  - Every value has to be between -1 and 11 otherwise a reset will be triggered
	 *  - The size of the object must never exceed int[steps][11]
	 *  Note:
	 *  
	 *  When int[][] = int[x][y] :
	 *   - The x marks the step number - 1
	 *   - The y marks the value the user inputs
	 *   - The [x, y] marks the values generated in the sequence
	 *  
	 *  The gSequence is only used when levelType equals LevelType.RANDOM and will update automatically
	 *  each step. This value can be reset but will cause the generator to generate a new sequence the next
	 *  time the level is loaded.
	 *  
	 *  The gSequence is designed to generate a random sequence every instance of game launch NOT every instance
	 *  of a Level use, in order to change that, call resetGSeq() after the level closes or before the level starts.
	 */
	private int[][] gSequence;

	/**
	 * canBeNull: Yes, levelName will default to "Level %n" while expression will be defaulted to "x"
	 * Restrictions:
	 *  - levelName: None
	 *  - expression: When expression is being used, the expression must be valid otherwise an InvalidExpressionException will be thrown
	 *  Note:
	 *  
	 *  levelName: The '%n' in the string will be replaced with the level number, this allows for dynamic level numbering
	 *  expression: The allowed operators are - + * and /, and ( ) brackets are allowed to force an expression to calculate in order
	 */
	private String levelName, expression;

	/**
	 * canBeNull: No, primitive type
	 * Restrictions:
	 * 	- solved: None
	 *  - needsRefresh: If needs refresh is true when a level call is made, that call will be cancelled
	 *  Note:
	 *  
	 *  solved: Only used as a display variable so it can be changed or altered as many times as you want
	 *  needsRefresh: Should only be changed to true if a manual refresh of the level is required due to an illegal variable change
	 */
	private boolean solved, needsRefresh = false;

	/**
	 * canBeNull: No, a null id will result in a new random id being generated, there for creating a new level
	 * Restrictions:
	 *  - All id's must be unique from other levels otherwise levels will be merged
	 *  Note:
	 *  
	 *  If a level is the same as another, but has a different id, the levels will then be saved in two seperate files.
	 *  The static method newFrom(Level) will clone the supplied Level and will change the id effectively doing what is said above.
	 */
	private UUID id;

	/**
	 * Main constructor containing all parameters needed to initialize the object
	 * 
	 * @param levelType		The type of level from the LevelType enum
	 * @param savedInfo		The 2D array of information needed for LevelType.SAVED levels
	 * @param gSec			The 2D array of information about the generated sequence, only used for LevelType.RANDOM
	 * @param levelNum		The number that will be assigned to the level (doesn't have to be unique)
	 * @param steps			The maximum amount of steps that can be taken in a level (only used in LevelType.RANDOM)
	 * @param levelName		The display name of the level being initialized ('%n' will be replaced with the level number by default)
	 * @param expression	The expression used to calculate the computer move in LevelType.CALCULATED
	 * @param solved		The current state of the level (only for display purposes, doesn't affect the level itself)
	 * @param id			The UUID for the level object, if any two ID's are the same the levels will be merged
	 */
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

	/**
	 * The following constructors are used in the creation of LevelType.DEFAULT, LevelType.CALCULATED and LevelType.RANDOM
	 * levels, the following rules apply to ALL the below constructors. For information about parameters, see the above variables
	 * for their detailed description and usage.
	 * 
	 * - It is assumed the level type is NOT LevelType.SAVED, if it is it WILL cause errors
	 * - If gSeq is not supplied a blank gSeq will be used instead
	 * - If a level name isn't supplied it will default to "Level %n" where '%n' is the level number
	 * - if no id is supplied a random one will be generated as the ID is required for the initialization of the object
	 */
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
		this(levelType, null, new int[0][0], levelNum, steps, levelName, expression, false, id);
	}

	public Level(LevelType levelType, int levelNum, int steps, String expression, UUID id) {
		this(levelType, null, new int[0][0], levelNum, steps, "Level %n", expression, false, id);
	}

	public Level(LevelType levelType, int levelNum, int steps, String levelName, String expression) {
		this(levelType, null, new int[0][0], levelNum, steps, levelName, expression, false, UUID.randomUUID());
	}

	public Level(LevelType levelType, int levelNum, int steps, String expression) {
		this(levelType, null, new int[0][0], levelNum, steps, "Level %n", expression, false, UUID.randomUUID());
	}
	/** END CONSTRUCTORS **/

	/**
	 * Gets the LevelType enum, if this value is null the level should NOT be used
	 * 
	 * @return	The LevelType enum of the Level object
	 */
	public LevelType getLevelType() {
		if (this.levelType == null)
			this.levelType = LevelType.DEFAULT;
		return this.levelType;
	}

	/**
	 * Gets the Object[][] of saved info that is available in the level, this can also be null.
	 * This value should be ignored unless the levelType is LevelType.SAVED
	 * 
	 * @return	The Object[][] AKA savedInfo or null
	 */
	public Object[][] getSavedInfo() {
		return this.savedInfo;
	}

	/**
	 * Gets the int[] of the current saved row for use with setting up a step
	 * Note:
	 * 	- 11 or above indicates a step that is illegal (not allowed)
	 *  - negative values indicate that the steps will be reduced by the n from the -n
	 *  - any other number indicates the step the computer will make
	 * 
	 * @return	The int[] from the savedInfo AKA the moves available to the player
	 */
	public int[] getCurrentRow() {
		if (this.savedInfo.length <= this.stepsTaken + 1) {
			this.solved = true;
			return new int[0];
		}
		Object[] transfer = this.savedInfo[this.stepsTaken + 1];
		int[] row = new int[transfer.length - 1];
		Main.debug("SIZE: " + this.stepsTaken);
		Main.debug(Arrays.toString(row));
		for (int i = 1; i < transfer.length; i++) {
			row[i - 1] = Integer.parseInt((String) transfer[i]);
		}
		return row;
	}

	/**
	 * Gets the number of the level
	 * 
	 * @return	The number of the Level object
	 */
	public int getLevelNum() {
		return this.levelNum;
	}

	/**
	 * Gets the name of the Level with the '%n' replaced with the level number
	 * 
	 * @return	The levelName variable with '%n' replaced with the levelNum
	 */
	public String getLevelName() {
		if (this.levelName == null)
			this.levelName = "Level %n";
		return this.levelName.replace("%n", levelNum + "");
	}

	/**
	 * Gets the Unique ID of the Level object, and throws InvalidLevelException if the level doesn't have one
	 * 
	 * @return	The UUID of the Level object
	 * @throws	InvalidLevelException if the UUID doesn't exist
	 */
	public UUID getID() throws InvalidLevelException {
		if (this.id == null)
			throw new InvalidLevelException("The level doesnt have an assigned ID");
		return id;
	}

	/**
	 * Gets the amount of steps that have been taken by the player
	 * 
	 * @return	The amount of stepsTaken
	 */
	public int getStepsTaken() {
		if (this.stepsTaken < 0)
			this.stepsTaken = 0;
		return stepsTaken;
	}

	/**
	 * Gets the Generated Sequence, this should only be used if levelType equals LevelType.RANDOM
	 * 
	 * @return	The gSequence from the Level object
	 */
	public int[][] getGeneratedSequence() {
		return gSequence;
	}

	/**
	 * Gets the String expression that is assigned to the Level object. This value CAN be null
	 * 
	 * @return	The String expression for the Level object or null if it doesnt exist
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Gets the amount of total steps that are allowed to be taken
	 * 
	 * @return	The amount of steps that can be taken
	 */
	public int getSteps() {
		return this.steps;
	}

	/**
	 * Gets whether or not the level needs to be manually refreshed due to an illegal variable change
	 * 
	 * @return	The boolean of whether or not the level needs a manual reset
	 */
	public boolean doesNeedsRefresh() {
		return needsRefresh;
	}

	/**
	 * Gets the solved state of the Level object
	 * 
	 * @return	The solved state of the Level object
	 */
	public boolean isSolved() {
		return solved;
	}

	/**
	 * Set the savedInfo of the Level object, if the value is null when the levelType is
	 * LevelType.SAVED then no changes will be made
	 * 
	 * @param savedInfo	The savedInfo to be used. refer to the variable documentation for more info
	 */
	public void setSavedInfo(Object[][] savedInfo) {
		if (savedInfo == null && this.levelType == LevelType.SAVED)
			return;
		this.savedInfo = savedInfo;
	}

	/**
	 * Set the Level number that is used for display purposed only
	 * 
	 * @param levelNum	The number to set the levelNumber to
	 */
	public void setLevelNum(int levelNum) {
		this.levelNum = levelNum;
	}

	/**
	 * Set the Level name that is used for display purposes only
	 * 
	 * @param levelName	The levelName to be used, '%n' is replaced with the levelNumber
	 */
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	/**
	 * Set the solved state of the Level, for display purposes
	 * 
	 * @param solved	The boolean solved state
	 */
	public void setSolved(boolean solved) {
		this.solved = solved;
	}

	/**
	 * Set the expression to be used by the Level when levelType equals LevelType.CALCULATED
	 * 
	 * @param expression	The string expression, refer to the variable documentation for restrictions
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * Checks if the players step made is valid for the computer to make a returning move
	 * 
	 * @param step	The number that the user has selected
	 * @return		true: The computer can make a returning move
	 * 				  false: The step is not valid there for the computer cannot make a returning move
	 */
	public boolean stepValid(int step) {
		if (this.steps <= this.stepsTaken) {
			Main.debug("SMALLER");
			if (this.levelType != LevelType.SAVED)
				return false;
		}
		if (this.levelType == LevelType.RANDOM) {
			if (this.gSequence.length < this.stepsTaken + 1) {
				this.addToGSeq();
			}
			int[] section = this.gSequence[this.stepsTaken];
			int count = 0;
			for (int i : section) {
				if (i == 11)
					continue;
				if (step == count)
					return true;
				count++;
			}
		} else if (this.levelType == LevelType.SAVED) {
			Main.debug("SAVED");
			if (this.savedInfo.length < this.stepsTaken + 1)
				return false;
			//                Main.debug("STEPS TAKEN: " + this.stepsTaken);
			Main.debug("PRINT: " + Arrays.toString(this.savedInfo[this.stepsTaken + 1]));
			int val = Integer.parseInt((String) this.savedInfo[this.stepsTaken + 1][step + 1]);
			Main.debug("NUM: " + val);
			switch (val) {
			case 11:
				return false;
			default:
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the players step made is correct for the computer to make a returning move and move to the next step
	 * 
	 * @param step	The step the player has made
	 * @return		TRUE: The step is correct, meaning there is no reset and the player can move to the next step
	 * 				FALSE: The step is incorrect there for indicating the level cannot proceed
	 */
	public boolean stepCorrect(int step) {
		if (!this.stepValid(step))
			return false;
		if (this.levelType == LevelType.RANDOM) {
			if (this.gSequence.length < this.stepsTaken + 1) {
				this.addToGSeq();
			}
			int[] section = this.gSequence[this.stepsTaken];
			int count = 0;
			for (int i : section) {
				if (i == 11)
					continue;
				if (step == count) {
					if (i > 0)
						return true;
				}
				count++;
			}
			return false;
		} else if (this.levelType == LevelType.CALCULATED) {
			if (this.savedInfo.length <= this.stepsTaken + 1)
				return false;
			int val = (int) this.savedInfo[this.stepsTaken + 1][step + 1];
			switch (val) {
			case 0:
			case 11:
				return false;
			default:
				return true;
			}
		} else {
			return true;
		}
	}

	/**
	 * Gets the move the computer will make in return to the players move
	 * 
	 * @param step	The number that the user has selected
	 * @return		0: Can be interpreted as a reset because all levels start at 0
	 * 				  -69: Step isn't valid there for the computer cannot make a move
	 * 				  else: The value of the computers move
	 */
	public int getComputerMove(int step) {
		if (this.stepsTaken == 0)
			return 0;
		if (step < 0 || step > 9)
			return -69;
		if (this.levelType == LevelType.DEFAULT)
			return step;
		if (this.levelType == LevelType.CALCULATED) {
			Equation e = new Equation(this.expression, step);
			int result = -1;
			try {
				e.evaluate();
				result = e.getRoundedResult();
				if (result < 0) {
					while (result < 0) {
						result += 10;
					}
				}
				if (result > 9) {
					while (result > 9) {
						result -= 10;
					}
				}
			} catch (InvalidExpressionException ex) {
				ex.printStackTrace();
				return -1;
			}
			return result;
		} else if (this.levelType == LevelType.RANDOM) {
			if (this.gSequence.length != (this.stepsTaken + 1)) {
				this.addToGSeq();
			}
			return this.gSequence[this.stepsTaken][step];
		} else { // SAVED
			if (this.savedInfo.length <= (this.stepsTaken + 1)) {
				return -69;
			} else if (this.savedInfo[this.stepsTaken].length < (step + 1)) {
				return -69;
			}
			int move = Integer.parseInt((String) this.savedInfo[this.stepsTaken - 1][step + 1]);
			if (move > 9) { // Shouldn't be possible, so a reset
				Main.debug("OH NO");
				this.reset(false);
				return this.getComputerMove(step);
			} else if (move < 0) {
				this.undo(move);
				return this.getComputerMove(step);
			} else {
				return move;
			}
		}
	}

	/**
	 * moves to the next step in the Level object
	 * 
	 * @deprecated	Found that it would mess with the gSequence so i
	 * 				replaced it with an updated method that also handles
	 * 				the gSequence
	 */
	public void nextStep() {
		this.stepsTaken += 1;
	}

	/**
	 * Sets the step and its relating variables relative to the previous step choice
	 * 
	 * @param previous	The number that the user last selected
	 * @return			0: Can be interpreted as a reset because all levels start at 0
	 * 					  -1: There was an unknown error setting the next step
	 * 					  -69: Step isn't valid there for the computer cannot make a move
	 * 					  else: The value of the computers move
	 */
	public int nextStep(int current) {
		if (!this.stepCorrect(current)) {
			return -69;
		}
		this.needsRefresh = true;
		int move = this.getComputerMove(current);
		Main.debug("MOVE: " + this.stepsTaken);
		this.stepsTaken++;
		this.solved = move == 9;
		return move;
	}

	/**
	 * Resets the Level object to its default state.
	 * 
	 * @param resetGSequence	Whether or not to include the gSequence in the reset
	 * 							  Refer to the variable documentation on gSequence usage
	 */
	public void reset(boolean resetGSequence) {
		this.previousStep = -1;
		this.stepsTaken = 0;
		this.solved = this.needsRefresh = false;
		if (resetGSequence) {
			this.gSequence = null;
		}
	}

	/**
	 * Add a new row to the gSequence if needed
	 * 
	 * WARNING: This should only be used by the Level object itself but can be called
	 * 			elsewhere, make sure you understand the usage of gSequence before using this
	 */
	public void addToGSeq() {
		if (this.gSequence.length >= this.stepsTaken)
			return;
		if (this.choices == 0)
			return;
		int loop = this.stepsTaken - this.gSequence.length;
		for (int l = 0; l < loop; l ++) {
			Random r = new Random();
			int choice = r.nextInt(9);
			int[] row = new int[10];
			for (int i = 0; i < row.length; i++) {
				row[i] = 11;
			}
			int[] chosen = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
			for (int i = 0; i < this.choices; i++) {
				if (i == 0) {
					int[] temp = new int[10-(i+ 1)];
					row[choice] = choice;
					for (int ii = 0; ii < temp.length; ii++) {
						if (ii <= choice) {
							temp[i] = chosen[i+ 1];
						} else {
							temp[i] = chosen[i];
						}
					}
					chosen = temp;
				} else {
					int choice2 = chosen[r.nextInt(chosen.length)];
					int[] temp = new int[10-(i+ 1)];
					row[choice2] = choice2;
					for (int ii = 0; ii < temp.length; ii++) {
						if (ii <= choice2) {
							temp[i] = chosen[i+ 1];
						} else {
							temp[i] = chosen[i];
						}
					}
					chosen = temp;
				}
			}
			int[][] temp = this.gSequence.clone();
			this.gSequence = new int[(temp.length + 1)][10];
			for (int i = 0; i < this.gSequence.length; i++) {
				if (i == (this.gSequence.length + 1)) { // Last
					this.gSequence[i] = row;
				} else {
					this.gSequence[i] = temp[i];
				}
			}
		}
	}

	/**
	 * Undoes the steps taken by the amount supplied, this needs to be followed up with
	 * an update otherwise the level might crash
	 * 
	 * @param steps	The amount of steps to go back by
	 */
	public void undo(int steps) {
		this.stepsTaken -= steps;
		this.stepsTaken = this.stepsTaken < 0 ? 0 : this.stepsTaken;
	}

	/**
	 * Gets the setup step AKA the computers move of the current step.
	 * NOTE: This does NOT update the step, it only returns a value.
	 * 
	 * @return	The setup step of the current step
	 */
	public int getStepSetup() {
		if (this.previousStep == -1)
			return 0;
		return this.stepsTaken == 0 ? 0 : this.gSequence[this.stepsTaken - 1][this.previousStep];
	}

	/**
	 * Gets a new Level object from the supplied Level object with a different id
	 * 
	 * @param level	The Level object to be duplicated
	 * @return		The new Level object
	 */
	public static Level newFrom(Level level) {
		try {
			Level val = (Level) level.clone();
			val.id = UUID.randomUUID();
			return val;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	/**
	 * Gets a string representation of the object for use with file IO
	 * 
	 * @return	The String representation of the Level object
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		if (this.getLevelType() == LevelType.SAVED) {
			b.append(this.levelNum + ",");
			b.append(this.levelName + ",");
			b.append(this.solved + ",");
			b.append(this.levelType.toString() + ",");
			b.append(this.steps + ",");
			b.append(ArrayUtils.intArrToString(this.gSequence) + ",");
			b.append(ArrayUtils.objArrToString(this.savedInfo));
		} else {
			b.append(this.levelNum + ",");
			b.append(this.levelName + ",");
			b.append(this.solved + ",");
			b.append(this.levelType.toString() + ",");
			b.append(this.steps + ",");
			b.append(this.expression);
		}
		return b.toString();
	}

}
