package com.puzzlegalaxy.slider.commands;

public interface CommandExecutor {

	public boolean onCommand(Command cmd, String label, String[] args);
	
}
