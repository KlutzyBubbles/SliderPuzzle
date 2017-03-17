package com.puzzlegalaxy.slider.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.puzzlegalaxy.slider.Main;
import com.puzzlegalaxy.slider.exceptions.InvalidCommandException;

public class CommandHandler {

	private static List<Command> commands = new ArrayList<Command>();

	public static void loadCommands(String path) throws InvalidCommandException {
		File folder = new File(path);
		File[] files = folder.listFiles();
		if (files.length == 0)
			return;
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory())
				continue;
			if (!f.getName().endsWith(".xml"))
				continue;
			loadCommand(f);
		}
	}

	public static void loadCommand(String file) throws InvalidCommandException {
		File f = new File(file);
		if (f.isDirectory())
			throw new InvalidCommandException("Command file is a directory (Code: 4)");
		if (!f.getName().endsWith(".xml"))
			throw new InvalidCommandException("Command file is not an XML (Code: 5)");
		loadCommand(new File(file));
	}

	public static void loadCommand(File file) throws InvalidCommandException {
		if (file == null) {
			throw new InvalidCommandException("Command file is null (Code: 0)");
		} else {
			try {
				Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
				NodeList nodeList = document.getElementsByTagName("command");
				for (int i = 0; i < nodeList.getLength(); i++) {
					String name = "";
					String description = "";
					List<String> aliases = new ArrayList<String>();
					Node node = nodeList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;
						name = element.getAttribute("name");
						NodeList nList = element.getChildNodes();
						for (int j = 0; j < nList.getLength(); j++) {
							Node n = nList.item(i);
							if (n.getNodeType() == Node.ELEMENT_NODE) {
								Element child = (Element) n;
								String display = child.getNodeName();
								if (display.equalsIgnoreCase("description")) {
									description = child.getTextContent();
								} else if (display.equalsIgnoreCase("alias")) {
									if (child.getTextContent().contains(" ")) {
										Main.debugLoop("Alias contains spaces, ignoring...");
									} else {
										aliases.add(child.getTextContent());
									}
								}
							}
						}
					}
					if (name == null || name.equals("")) {
						Main.debugLoop("Command read doesnt contain a valid name (Index: " + i + ")");
					} else if (description == null) {
						Main.debugLoop("Command read contians a null description (Index: " + i + ")");
					} else {
						if (getCommand(name) != null) {
							Main.debugLoop("Command read already is registered (Index: " + i + ")");
						} else {
							Command c = new Command(name, description, aliases);
							commands.add(c);
						}
					}
				}
			} catch (ParserConfigurationException e) {
				throw new InvalidCommandException("Could not parse command XML (Code: 1)");
			} catch (IOException e) {
				throw new InvalidCommandException("Could not process IO for XML file (Code: 2)");
			} catch (SAXException e) {
				throw new InvalidCommandException("Could not process SAX for XML file (Code: 3)");
			}
		}
	}

	public static Command getCommand(String name) {
		if (commands == null || commands.isEmpty()) {
			return null;
		}
		for (Command c : commands) {
			if (c.getName().equalsIgnoreCase(name))
				return c;
		}
		return null;
	}
	
	public static boolean executeCommand(String full) {
		if (full.contains(" ")) {
			String[] split = full.split(" ");
			Command c = getCommand(split[0]);
			if (c == null)
				return false;
			String[] args = new String[split.length - 1];
			for (int i = 1; i < split.length; i++) {
				args[i - 1] = split[i];
			}
			return c.dispatchCommand(args);
		} else {
			Command c = getCommand(full);
			if (c == null)
				return false;
			return c.dispatchCommand();
		}
	}

}
