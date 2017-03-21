package com.puzzlegalaxy.slider.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.puzzlegalaxy.slider.C;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -20769342430119923L;

	public static boolean computer, player, sliding = false;
	
	private MainPanel panel;
	
	public MainFrame() {
		this.setTitle("Slider Puzzle ALPHA v" + C.VERISON);
		this.setSize(800, 650);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.panel = new MainPanel();
		this.add(this.panel, BorderLayout.NORTH);
		this.add(new CreditPanel(), BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}
	
	public void updateSlider() {
		this.panel.updateSlider();
	}
	
}
