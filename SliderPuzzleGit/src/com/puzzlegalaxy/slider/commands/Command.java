package com.puzzlegalaxy.slider.commands;

import java.util.ArrayList;
import java.util.List;

public class Command {
	
	private final String name;
	private List<String> aliases;
	private String description;
	private CommandExecutor commandExecutor;
	
	public Command(String name) {
		this(name, "No Description", new ArrayList<String>());
	}
	
	public Command(String name, String description, List<String> aliases) {
		this.name = name;
		this.description = description;
		this.aliases = aliases;
	}
	
	public String getName() {
		return this.name;
	}

	public List<String> getAliases() {
		return this.aliases;
	}

	public String getDescription() {
		return this.description;
	}

	public CommandExecutor getCommandExecutor() {
		return this.commandExecutor;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCommandExecutor(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor == null ? this.commandExecutor : commandExecutor;
	}
	
	public boolean dispatchCommand() {
		return this.dispatchCommand(new String[0]);
	}
	
	public boolean dispatchCommand(String[] args) {
		if (this.commandExecutor == null)
			return false;
		return commandExecutor.onCommand(this, name, args);
	}
	
}
