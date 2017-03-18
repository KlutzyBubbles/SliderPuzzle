package com.puzzlegalaxy.slider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.puzzlegalaxy.slider.commands.Command;
import com.puzzlegalaxy.slider.commands.CommandHandler;
import com.puzzlegalaxy.slider.exceptions.InvalidCommandException;
import com.puzzlegalaxy.slider.executors.ExitCommand;
import com.puzzlegalaxy.slider.levels.Level;
import com.puzzlegalaxy.slider.levels.LevelManager;
import com.puzzlegalaxy.slider.utils.ResourceUtil;

public class Main {

	public static boolean debug = true;
	public static boolean debugLoop = true;
	public static LevelManager lm;
	private static Scanner s = new Scanner(System.in);

	public static void main(String[] args) {
		
		ResourceUtil.addCommandResource("internal");
		ResourceUtil.addLevelResource("10703474-8e8b-48f0-b236-a0441b8495eb");
		ResourceUtil.addLevelResource("321b8b1f-78b1-461f-9d8d-f9a01bab180a");
		ResourceUtil.addLevelResource("3b6669b2-5c31-4440-b2ad-043e5cf2afd8");
		ResourceUtil.addLevelResource("82fce33b-153a-4470-a23b-2571a001d4cd");
		ResourceUtil.addLevelResource("f2052305-3a17-454e-a425-0885c29f44ad");
		
//		transfer("src/commands/", ".xml", "commands", ResourceUtil.getCommandResources());
//		transfer("src/levels/", ".level", "levels", ResourceUtil.getLevelResources());
		
		String fs = System.getProperty("file.separator");
		String path = "";
		try {
			path = getProgramPath();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (path.equals("")) {
			Main.debug("Something went wrong");
			finish();
		}
		String dir = path + fs + "commands" + fs;
		
		try {
			CommandHandler.loadCommands(dir);
		} catch (InvalidCommandException e) {
			e.printStackTrace();
			Main.debug("Commands couldn't be loaded");
		}
		Command exit = CommandHandler.getCommand("exit");
		if (exit != null) {
			exit.setCommandExecutor(new ExitCommand());
			Main.debug("Exit command registered");
		}

		lm = new LevelManager();
		lm.loadLevels();
		
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

	public static void transfer(String folder, String extension, String targetName, List<String> resources) {
		InputStream i = null;
		OutputStream o = null;
		try {
			String fs = System.getProperty("file.separator");
			String path = getProgramPath();
			String dir = path + fs + targetName + fs;
			Main.debug(dir);

			for (String name : resources) {
				i = new FileInputStream(folder + fs + name + extension);
				File f = new File(dir + name + extension);
				f.getParentFile().mkdir();
				f.createNewFile();
				o = new FileOutputStream(f);
				
				int read = 0;
				byte[] bytes = new byte[1024];
				while ((read = i.read(bytes)) != -1) {
					o.write(bytes, 0, read);
				}
				
				i.close();
				o.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (i != null) {
					i.close();
				}
				if (o != null) {
					o.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getProgramPath() throws UnsupportedEncodingException {
		URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
		String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
		return new File(jarPath).getParentFile().getPath();
	}

}
