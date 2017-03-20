package com.puzzlegalaxy.slider.executors;

import com.puzzlegalaxy.slider.Main;
import com.puzzlegalaxy.slider.commands.Command;
import com.puzzlegalaxy.slider.commands.CommandExecutor;

public class LevelCommand implements CommandExecutor {

	@Override
	public boolean onCommand(Command cmd, String label, String[] args) {
		if (args.length > 0) {
			String text = args[0];
			int num;
			try {
				num = Integer.parseInt(text);
			} catch (NumberFormatException e) {
				System.out.println("Please enter only a level NUMBER");
				return false;
			}
			if (!Main.lm.levelExists(num)) {
				System.out.println("A level with that number doesnt exist");
				return false;
			} else {
				Main.newLevel(num);
				System.out.println("Level changed to level number #" + num);
				return true;
			}
		}
		return false;
	}

}
