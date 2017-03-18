package com.puzzlegalaxy.slider.utils;

import java.util.ArrayList;
import java.util.List;

public class ResourceUtil {

	private static List<String> commandResources = new ArrayList<String>();
	private static List<String> levelResources = new ArrayList<String>();
	
	public static List<String> getCommandResources() {
		return commandResources;
	}
	
	public static List<String> getLevelResources() {
		return levelResources;
	}
	
	public static void setCommandResources(List<String> commandResources) {
		if (commandResources == null)
			return;
		ResourceUtil.commandResources = commandResources;
	}
	
	public static void setLevelResources(List<String> levelResources) {
		if (levelResources == null)
			return;
		ResourceUtil.levelResources = levelResources;
	}
	
	public static void addCommandResource(String s) {
		if (s == null)
			return;
		ResourceUtil.commandResources.add(s);
	}
	
	public static void addLevelResource(String s) {
		if (s == null)
			return;
		ResourceUtil.levelResources.add(s);
	}
	
	public static void clearCommandResources() {
		ResourceUtil.commandResources.clear();
	}
	
	public static void clearLevelResources() {
		ResourceUtil.levelResources.clear();
	}
	
}
