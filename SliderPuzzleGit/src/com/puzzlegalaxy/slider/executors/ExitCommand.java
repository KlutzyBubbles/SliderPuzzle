package com.puzzlegalaxy.slider.executors;

import com.puzzlegalaxy.slider.Main;
import com.puzzlegalaxy.slider.commands.Command;
import com.puzzlegalaxy.slider.commands.CommandExecutor;

public class ExitCommand implements CommandExecutor {

	@Override
	public boolean onCommand(Command cmd, String label, String[] args) {
		Main.finish();
		return true;
	}

}
