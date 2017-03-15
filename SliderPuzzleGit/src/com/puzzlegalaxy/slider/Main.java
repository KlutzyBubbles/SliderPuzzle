package com.puzzlegalaxy.slider;

import java.util.UUID;

import com.puzzlegalaxy.slider.exceptions.InvalidExpressionException;
import com.puzzlegalaxy.slider.levels.LevelManager;
import com.puzzlegalaxy.slider.utils.Equation;

public class Main {

	public static boolean debug = true;
	public static boolean debugLoop = true;
	public static LevelManager lm;

	public static void main(String[] args) {
		Equation e = new Equation("((6+2)*3)/6");
		try
		{
			System.out.println("SOMETHING" + e.evaluate());
			System.out.println(e.getRoundedResult());
		}
		catch (InvalidExpressionException ex)
		{
			ex.printStackTrace();
			System.out.println("Something");
			System.exit(1);
		}
		lm = new LevelManager();
		lm.loadLevels();
		System.out.println(lm.getLevelCount());
		lm.printLevels();
		System.out.println(UUID.randomUUID().toString());
	}

	public static void debug(String msg) {
		if (debug) {
			System.out.println(msg);
		}
	}
	
	public static void debugLoop(String msg) {
		if (debugLoop) {
			System.out.println(msg);
		}
	}

}
