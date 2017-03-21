package com.puzzlegalaxy.slider.gui;

import java.util.Hashtable;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JSlider;

import com.puzzlegalaxy.slider.C;
import com.puzzlegalaxy.slider.Main;
import com.puzzlegalaxy.slider.gui.events.UserSliderListener;
import com.puzzlegalaxy.slider.levels.LevelManager;

public class UserSlider extends JSlider {

	private static final long serialVersionUID = -644838921546618023L;

	private Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();

	public UserSlider() {
		super(0, 9, 0);
		this.updateLabels();
		this.setZero();
		this.addChangeListener(new UserSliderListener());
	}

	public void updateLabels() {
		this.labels = new Hashtable<Integer, JLabel>();
		int[] row = LevelManager.getCurrentLevel().getSolvingRow();
		if (row == null) { // puzzle is solved
			row = C.SOLVED_ARRAY;
		}
		for (int i = 0; i < row.length; i++) {
			if (row[i] <= 10 && row[i] > 0) {
				this.labels.put(i, new JLabel("" + i));
			} else {
				if (row[i] == -1) {
					this.labels.put(i, new JLabel("" + i));
				} else {
				this.labels.put(i, new JLabel(" "));
				}
			}
		}
		if (this.labels == null || this.labels.size() == 0)
			this.updateLabels(1);
		this.setLabelTable(this.labels);
		this.setPaintTicks(true);
		this.setPaintLabels(true);
		this.setSnapToTicks(true);
	}
	
	public void updateLabels(int attempt) {
		Main.debug("ATTEMPT: " + attempt);
		if (attempt > 5)
			return;
		this.labels = new Hashtable<Integer, JLabel>();
		int[] row = LevelManager.getCurrentLevel().getSolvingRow();
		Main.debug("ROW SIZE: " + row.length);
		for (int i = 0; i < row.length; i++) {
			if (row[i] <= 10 && row[i] > 0) {
				this.labels.put(i, new JLabel("" + i));
			} else {
				this.labels.put(i, new JLabel(" "));
			}
		}
		if (this.labels == null || this.labels.size() == 0)
			this.updateLabels(attempt + 1);
		this.setLabelTable(this.labels);
		this.setPaintTicks(true);
		this.setPaintLabels(true);
		this.setSnapToTicks(true);
	}

	public void setZero() {
		this.setValue(0);
	}

	public void setNearest() {
		Main.debug(this.labels.get(this.getValue()).getText() + " TEXT");
		if (!this.labels.get(this.getValue()).getText().equals(" "))
			return;
		int countDown = 0, valueDown = 0;
		if (this.getValue() == this.labels.size() - 1) { // search down
			for (int i = this.getValue(); i < this.labels.size() - 1; i++) {
				if (!this.labels.get(i).getText().equals(" ")) {
					this.setValue(i);
					return;
				}
			}
		} else {
			int countUp = 0, valueUp = 0;
			if (this.getValue() == 0) { // search up
				for (int i = this.labels.size() - 1; i > this.getValue(); i--) {
					if (!this.labels.get(i).getText().equals(" ")) {
						this.setValue(i);
						return;
					}
				}
			} else { // both ways
				for (int i = this.getValue(); i < this.labels.size() - 1; i++) {
					if (!this.labels.get(i).getText().equals(" ")) {
						valueUp = i;
						break;
					}
					countUp++;
				}
				for (int i = this.labels.size() - 1; i > this.getValue(); i--) {
					if (!this.labels.get(i).getText().equals(" ")) {
						valueDown = i;
						break;
					}
					countDown--;
				}
				if (countDown > countUp) {
					this.setValue(valueUp);
				} else {
					this.setValue(valueDown);
				}
			}
		}
	}
	
	public Map<Integer, JLabel> getLabels() {
		return this.labels != null ? this.labels : new Hashtable<Integer, JLabel>();
	}

}
