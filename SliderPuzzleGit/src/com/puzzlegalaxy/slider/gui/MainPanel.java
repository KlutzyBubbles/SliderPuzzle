package com.puzzlegalaxy.slider.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.puzzlegalaxy.slider.Main;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = -1519947308069033178L;
	
	private UserSlider us;
	private JScrollPane scrollBar;
	
	public MainPanel() {
		this.setPreferredSize(new Dimension(800, 650));
		this.setLayout(new BorderLayout());
		
		this.us = new UserSlider();
		this.scrollBar = new JScrollPane(Main.debugText);
		
		this.add(this.us, BorderLayout.WEST);
		this.add(this.scrollBar, BorderLayout.EAST);
	}
	
	public void updateSlider() {
		this.us.updateLabels();
	}
	
}