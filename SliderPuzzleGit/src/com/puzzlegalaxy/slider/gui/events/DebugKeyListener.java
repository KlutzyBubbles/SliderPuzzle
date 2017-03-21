package com.puzzlegalaxy.slider.gui.events;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.puzzlegalaxy.slider.Main;

public class DebugKeyListener implements KeyListener {

	private StringBuilder b = new StringBuilder();
	
	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			return;
		char c = e.getKeyChar();
		if (c == '\n')
			return;
		if (c != KeyEvent.CHAR_UNDEFINED) {
			//Main.debug("NOT UNDEFINED: " + c);
			b.append(c);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (b.length() > 0) {
				Main.debug("LENGTH GREATER");
				String text = b.toString();
				b.setLength(0);
				Main.debug("TEXT: " + text);
				Main.testMove(text);
			} else {
				Main.debug("PLEASE CONSUME ME");
				e.consume();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	
	
}
