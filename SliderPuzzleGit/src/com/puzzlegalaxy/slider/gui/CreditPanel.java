package com.puzzlegalaxy.slider.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.puzzlegalaxy.slider.C;

public class CreditPanel extends JPanel {

		private static final long serialVersionUID = 3503032462586917441L;
		
		public CreditPanel() {
			JLabel copy = new JLabel("Written by Lee Tzilantonis, © 2017");
			JLabel version = new JLabel("Version: " + C.VERISON);
			copy.setToolTipText("Email: LTzilantonis@gmail.com");
			version.setToolTipText("Currently in ALPHA");
			
			this.add(copy, BorderLayout.EAST);
			this.add(version, BorderLayout.WEST);
		}
		
	}