package com.puzzlegalaxy.slider.gui.events;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.puzzlegalaxy.slider.Main;
import com.puzzlegalaxy.slider.gui.MainFrame;
import com.puzzlegalaxy.slider.gui.UserSlider;
import com.puzzlegalaxy.slider.levels.LevelManager;

public class UserSliderListener implements ChangeListener {

	@Override
	public void stateChanged(ChangeEvent e) {
		if (!(e.getSource() instanceof UserSlider))
			return;
		UserSlider s = (UserSlider) e.getSource();
		if (!LevelManager.updated) {
			s.updateLabels();
			LevelManager.updated = true;
		}
		if (!s.getValueIsAdjusting()) {
			MainFrame.sliding = false;
			if (s.getLabels().get(s.getValue()).getText().equals(" "))  {
				s.setNearest();
			} else {
				Main.doMove(s.getValue(), 0);
			}
		} else {
			MainFrame.sliding = true;
		}
	}
	
}
