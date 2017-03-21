package com.puzzlegalaxy.slider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import com.puzzlegalaxy.slider.commands.Command;
import com.puzzlegalaxy.slider.commands.CommandHandler;
import com.puzzlegalaxy.slider.exceptions.InvalidCommandException;
import com.puzzlegalaxy.slider.executors.ExitCommand;
import com.puzzlegalaxy.slider.executors.LevelCommand;
import com.puzzlegalaxy.slider.gui.DebugOutputStream;
import com.puzzlegalaxy.slider.gui.MainFrame;
import com.puzzlegalaxy.slider.gui.events.DebugKeyListener;
import com.puzzlegalaxy.slider.levels.Level;
import com.puzzlegalaxy.slider.levels.LevelManager;
import com.puzzlegalaxy.slider.utils.ResourceUtil;

public class Main {

	public static boolean debug = false;
	public static boolean debugLoop = false;
	
	private static LevelManager lm;

	public static MainFrame f;
	public static JTextArea debugText;
	
	public static void main(String[] args) {
		
		ResourceUtil.addCommandResource("internal");
		ResourceUtil.addLevelResource("10703474-8e8b-48f0-b236-a0441b8495eb");
		ResourceUtil.addLevelResource("321b8b1f-78b1-461f-9d8d-f9a01bab180a");
		ResourceUtil.addLevelResource("3b6669b2-5c31-4440-b2ad-043e5cf2afd8");
		ResourceUtil.addLevelResource("82fce33b-153a-4470-a23b-2571a001d4cd");
		ResourceUtil.addLevelResource("f2052305-3a17-454e-a425-0885c29f44ad");
		
		//transfer("src/commands/", ".xml", "commands", ResourceUtil.getCommandResources());
		//transfer("src/levels/", ".level", "levels", ResourceUtil.getLevelResources());

		String fs = System.getProperty("file.separator");
		String path = "";
		try {
			path = getProgramPath();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (path.equals("")) {
			Main.debug(C.LOADING_ERROR);
			finish();
		}
		String dir = path + fs + "commands" + fs;

		try {
			CommandHandler.loadCommands(dir);
		} catch (InvalidCommandException e) {
			e.printStackTrace();
			Main.debug(C.COMMAND_LOADING_ERROR);
		}
		Command exit = CommandHandler.getCommand("exit");
		if (exit != null) {
			exit.setCommandExecutor(new ExitCommand());
		}
		Command level = CommandHandler.getCommand("level");
		if (level != null) {
			level.setCommandExecutor(new LevelCommand());
		}
		if (level != null && exit != null) {
			Main.debug(C.COMMANDS_REGISTERED);
		} else {
			Main.debug(C.COMMANDS_PART_REGISTERED);
		}

		lm = new LevelManager();
		lm.loadLevels();

		debugText = new JTextArea();
		debugText.setRows(50);
		debugText.setColumns(50);
		debugText.addKeyListener(new DebugKeyListener());
		f = new MainFrame();
		
		PrintStream print = new PrintStream(new DebugOutputStream(debugText));
		System.setOut(print);
		System.setErr(print);
		
		start();
	}

	public static void start() {
		LevelManager.setCurrentLevel(lm.getLevel(0));
		f.updateSlider();
	}
	
	public static boolean another() {
		int result = JOptionPane.showConfirmDialog(f, C.KEEP_PLAYING_QUESTION, C.KEEP_PLAYING_TITLE, JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			LevelManager.getCurrentLevel().setSolved(false);
			LevelManager.getCurrentLevel().reset(true);
			return true;
		}
		return false;
	}

	public static boolean newLevel(int num) {
		Level level = lm.getLevel(num);
		if (level == null)
			return false;
		level.setSolved(false);
		level.reset(false);
		LevelManager.setCurrentLevel(level);
		f.updateSlider();
		return true;
	}
	
	public static void testMove(String text) {
		if (text.startsWith(".")) {
			boolean executed = CommandHandler.executeCommand(text.substring(1, text.length()));
			if (!executed)
				System.out.println(C.COMMAND_FAILED);
			return;
		}
		doMove(text, 0);
	}

	public static void doMove(String text, int loop) {
		f.updateSlider();
		int move;
		try {
			move = Integer.parseInt(text);
		} catch (NumberFormatException e) {
			System.out.println(C.MOVE_UNKNOWN);
			return;
		}
		if (move < 1 || move > 9) {
			System.out.println(C.MOVE_OUT_OF_BOUNDS);
			return;
		}
		debug("LEVEL: " + LevelManager.getCurrentLevel().toString());
		if (!LevelManager.getCurrentLevel().stepValid(move)) {
			System.out.println(C.MOVE_INVALID);
			if (LevelManager.getCurrentLevel().outOfSteps()) {
				LevelManager.getCurrentLevel().reset(false);
				if (loop > 5)
					finish();
				doMove(text, loop++);
			}
		} else if (!LevelManager.getCurrentLevel().stepCorrect(move)) {
			System.out.println(C.MOVE_NOT_CORRECT);
		} else {
			System.out.println(C.COMPUTER_MOVE + LevelManager.getCurrentLevel().nextStep(move));
			if (LevelManager.getCurrentLevel().isSolved()) {
				System.out.println(C.LEVEL_SOLVED);
				if (another())
					f.updateSlider();
				else
					finish();
			}
		}
		f.updateSlider();
	}
	
	public static void doMove(int move, int loop) {
		f.updateSlider();
		if (move < 1 || move > 9) {
			System.out.println(C.MOVE_OUT_OF_BOUNDS);
			return;
		}
		debug("LEVEL: " + LevelManager.getCurrentLevel().toString());
		if (!LevelManager.getCurrentLevel().stepValid(move)) {
			System.out.println(C.MOVE_INVALID);
			if (LevelManager.getCurrentLevel().outOfSteps()) {
				LevelManager.getCurrentLevel().reset(false);
				if (loop > 5)
					finish();
				doMove(move, loop++);
			}
		} else if (!LevelManager.getCurrentLevel().stepCorrect(move)) {
			System.out.println(C.MOVE_NOT_CORRECT);
		} else {
			System.out.println(C.COMPUTER_MOVE + LevelManager.getCurrentLevel().nextStep(move));
			if (LevelManager.getCurrentLevel().isSolved()) {
				System.out.println(C.LEVEL_SOLVED);
				if (another())
					f.updateSlider();
				else
					finish();
			}
		}
		f.updateSlider();
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
	
	public static LevelManager getLevelManager() {
		return lm != null ? lm : (lm = new LevelManager());
	}

}
