package com.puzzlegalaxy.slider;

import java.util.Arrays;
import java.util.Scanner;

import com.puzzlegalaxy.slider.commands.Command;
import com.puzzlegalaxy.slider.commands.CommandHandler;
import com.puzzlegalaxy.slider.exceptions.InvalidCommandException;
import com.puzzlegalaxy.slider.executors.ExitCommand;
import com.puzzlegalaxy.slider.levels.Level;
import com.puzzlegalaxy.slider.levels.LevelManager;

public class Main {

	public static boolean debug = true;
	public static boolean debugLoop = true;
	public static LevelManager lm;
	private static Scanner s = new Scanner(System.in);

	public static void main(String[] args) {
		lm = new LevelManager();
		lm.loadLevels();

		try {
			CommandHandler.loadCommands("src/commands/");
		} catch (InvalidCommandException e) {
			e.printStackTrace();
			Main.debug("Commands couldn't be loaded");
		}
		Command exit = CommandHandler.getCommand("exit");
		if (exit != null) {
			exit.setCommandExecutor(new ExitCommand());
			Main.debug("Exit command registered");
		}
		
		int n = 1;
		Level l = lm.getLevel(n);
		while (!l.isSolved()) {
			debug("SETUP: " + l.getStepSetup());
			debug("Enter your move: ");
			debug("ROW: " + Arrays.toString(l.getCurrentRow()));
			if (l.getCurrentRow().length == 0) {
				debug("YAY You solved it");
			} else {
				String text = s.next();
				if (text.startsWith(".")) {
					boolean executed = CommandHandler.executeCommand(text.substring(1, text.length()));
					if (!executed)
						Main.debug("Damn the command didnt work");
				}
				int move;
				try {
					move = Integer.parseInt(text);
				} catch (NumberFormatException e) {
					debug("Please enter a number or a command");
					return;
				}
				if (move == 59) {
					return;
				}
				debug("LEVEL: " + l.toString());
				if (!l.stepValid(move)) {
					debug("Your move wasnt valid");
				} else if (!l.stepCorrect(move)) {
					debug("Your move wasnt correct");
				} else {
					debug("Computer Move: " + l.nextStep(move));
					if (l.isSolved()) {
						debug("YAY You solved it");
					}
				}
			}
		}
		s.close();
	}

	public static void debug(String msg) {
		if (debug) {
			StackTraceElement s = Thread.currentThread().getStackTrace()[2];
			String[] split = s.getClassName().split("\\.");
			System.out.println(split[split.length - 1] + "#" + s.getMethodName() + "-" + s.getLineNumber() + ": " + msg);
		}
	}
	
	public static void debugLoop(String msg) {
		if (debugLoop) {
			StackTraceElement s = Thread.currentThread().getStackTrace()[2];
			String[] split = s.getClassName().split("\\.");
			System.out.println(split[split.length - 1] + "#" + s.getMethodName() + "-" + s.getLineNumber() + ": " + msg);
		}
	}
	
	public static void finish() {
		s.close();
		System.exit(0);
	}

}
