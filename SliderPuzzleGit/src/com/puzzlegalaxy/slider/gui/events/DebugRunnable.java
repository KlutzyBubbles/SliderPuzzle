package com.puzzlegalaxy.slider.gui.events;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTextArea;

public class DebugRunnable implements Runnable {

	private static final String EOL1 = "\n";
	private static final String EOL2 = System.getProperty("line.separator", EOL1);

	private final JTextArea textArea;
	private final int maxLines;
	private final LinkedList<Integer> lengths;
	private final List<String> values;

	private int curLength;
	private boolean clear;
	private boolean queue;

	public DebugRunnable(JTextArea txtara, int maxlin) {
		textArea = txtara;
		maxLines = maxlin;
		lengths = new LinkedList<Integer>();
		values = new ArrayList<String>();

		curLength = 0;
		clear = false;
		queue = true;
	}

	public synchronized void append(String val) {
		values.add(val);
		if (queue) {
			queue = false;
			EventQueue.invokeLater(this);
		}
	}

	public synchronized void clear() {
		clear = true;
		curLength = 0;
		lengths.clear();
		values.clear();
		if (queue) {
			queue = false;
			EventQueue.invokeLater(this);
		}
	}

	@Override
	public synchronized void run() {
		if (clear)
			textArea.setText("");
		for (String val: values) {
			curLength += val.length();
			if (val.endsWith(EOL1) || val.endsWith(EOL2)) {
				if (lengths.size()>=maxLines)
					textArea.replaceRange("", 0, lengths.removeFirst());
				lengths.addLast(curLength);
				curLength=0;
			}
			textArea.append(val);
		}
		values.clear();
		clear = false;
		queue = true;
	}
}
